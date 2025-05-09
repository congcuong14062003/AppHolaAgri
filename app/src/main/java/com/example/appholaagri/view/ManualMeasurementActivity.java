package com.example.appholaagri.view;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.DirectMeasurement.DirectMeasurementRequest;
import com.example.appholaagri.model.SoilManualInitFormModel.SoilManualInitFormResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualMeasurementActivity extends AppCompatActivity {
    private UsbManager usbManager;
    private UsbSerialPort usbSerialPort;
    private ExecutorService executor;
    private Handler handler;
    private TextView humidityText;
    private TextView temperatureText;
    private TextView ecText;
    private TextView phText;
    private TextView nitrogenText;
    private TextView phosphorusText;
    private TextView potassiumText;
    private TextView usbStatusText;
    ImageView backBtnReview;
    private Button readButton;
    private List<SoilManualInitFormResponse.SensorInfo> sensorList;
    private Spinner spinnerSensor;
    private BroadcastReceiver usbPermissionReceiver;
    private BroadcastReceiver usbDetachedReceiver; // Receiver cho sự kiện USB bị rút
    private String token;
    private SoilManualInitFormResponse soilManualInitFormResponse;
    private static final String ACTION_USB_PERMISSION = "com.example.soilsensor.USB_PERMISSION";
    private DirectMeasurementRequest directMeasurementRequest;
    private boolean isPermissionRequestPending = false;
    private boolean isMeasuring = false; // Cờ để kiểm soát trạng thái đo
    private boolean isDeviceConnected = true; // Cờ để kiểm soát trạng thái thiết bị USB

    public static class UsbPermissionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ManualMeasurementActivity", "BroadcastReceiver onReceive called with action: " + intent.getAction());
            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean permissionGranted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                Log.d("ManualMeasurementActivity", "Permission granted: " + permissionGranted + ", Device: " + (device != null ? device.getDeviceName() : "null"));
                if (context instanceof ManualMeasurementActivity) {
                    ManualMeasurementActivity activity = (ManualMeasurementActivity) context;
                    activity.isPermissionRequestPending = false;
                    if (permissionGranted && device != null) {
                        Log.d("ManualMeasurementActivity", "Permission granted, checking USB devices");
                        activity.handler.post(() -> activity.checkUsbDevices());
                    } else {
                        activity.handler.post(() -> {
                            activity.usbStatusText.setText("Quyền USB bị từ chối");
                            activity.readButton.setEnabled(false);
                            activity.findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                        });
                        Toast.makeText(context, "USB Permission denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("ManualMeasurementActivity", "Context is not ManualMeasurementActivity");
                }
            } else {
                Log.e("ManualMeasurementActivity", "Unexpected action received: " + intent.getAction());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manual_measurement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", null);

        soilManualInitFormResponse = new SoilManualInitFormResponse();
        directMeasurementRequest = new DirectMeasurementRequest();

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        backBtnReview = findViewById(R.id.backBtnReview);
        humidityText = findViewById(R.id.humidity_text);
        temperatureText = findViewById(R.id.temperature_text);
        ecText = findViewById(R.id.ec_text);
        phText = findViewById(R.id.ph_text);
        nitrogenText = findViewById(R.id.nitrogen_text);
        phosphorusText = findViewById(R.id.phosphorus_text);
        potassiumText = findViewById(R.id.potassium_text);
        readButton = findViewById(R.id.read_button);
        spinnerSensor = findViewById(R.id.spinnerSensor);
        usbStatusText = findViewById(R.id.usb_status_text);
        Button retryButton = findViewById(R.id.retry_button);
        usbStatusText.setText("Đang kiểm tra thiết bị USB...");
        retryButton.setVisibility(View.GONE);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        // Đăng ký BroadcastReceiver cho sự kiện USB permission
        usbPermissionReceiver = new UsbPermissionReceiver();
        IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(usbPermissionReceiver, permissionFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(usbPermissionReceiver, permissionFilter);
        }

        // Đăng ký BroadcastReceiver cho sự kiện USB bị rút
        usbDetachedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                    Log.d("ManualMeasurementActivity", "USB device detached");
                    isDeviceConnected = false;
                    if (isMeasuring) {
                        handler.post(() -> {
                            usbStatusText.setText("Thiết bị USB đã bị ngắt kết nối");
                            readButton.setVisibility(View.VISIBLE);
                            isMeasuring = false;
                            Toast.makeText(ManualMeasurementActivity.this, "Thiết bị USB đã bị ngắt kết nối", Toast.LENGTH_LONG).show();
                        });
                    }
                    checkUsbDevices(); // Kiểm tra lại thiết bị sau khi ngắt
                }
            }
        };
        IntentFilter detachedFilter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(usbDetachedReceiver, detachedFilter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(usbDetachedReceiver, detachedFilter);
        }

        backBtnReview.setOnClickListener(view -> finish());
        readButton.setEnabled(false);

        retryButton.setOnClickListener(v -> {
            Log.d("ManualMeasurementActivity", "Retry button clicked");
            if (!isMeasuring) {
                usbStatusText.setText("Đang kiểm tra thiết bị USB...");
                retryButton.setVisibility(View.GONE);
                checkUsbDevices();
            }
        });

        checkUsbDevices();

        readButton.setOnClickListener(v -> readSensorData());

        getDataPlant();
    }

    public void getDataPlant() {
        executor.execute(() -> {
            ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
            Call<ApiResponse<SoilManualInitFormResponse>> call = apiInterface.initFormPlantByQrCode(token, "");
            call.enqueue(new Callback<ApiResponse<SoilManualInitFormResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<SoilManualInitFormResponse>> call, Response<ApiResponse<SoilManualInitFormResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<SoilManualInitFormResponse> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            soilManualInitFormResponse = apiResponse.getData();
                            handler.post(() -> updateListSensor());
                        } else {
                            Log.e("ManualMeasurementActivity", "API response unsuccessful: " + apiResponse.getMessage());
                        }
                    } else {
                        Log.e("ManualMeasurementActivity", "API response unsuccessful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<SoilManualInitFormResponse>> call, Throwable t) {
                    Log.e("PlantInformationActivity", "Error: " + t.getMessage());
                    handler.post(() -> CustomToast.showCustomToast(ManualMeasurementActivity.this, "Lỗi kết nối: " + t.getMessage()));
                }
            });
        });
    }

    private void updateListSensor() {
        if (soilManualInitFormResponse == null || soilManualInitFormResponse.getListSensor() == null) {
            CustomToast.showCustomToast(this, "Không có danh sách cảm biến để hiển thị!");
            return;
        }

        sensorList = soilManualInitFormResponse.getListSensor();
        List<String> sensorNames = new ArrayList<>();

        for (SoilManualInitFormResponse.SensorInfo sensor : sensorList) {
            if (sensor.getNameSensor() != null) {
                sensorNames.add(sensor.getNameSensor());
            } else {
                sensorNames.add("Cảm biến không tên");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sensorNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (spinnerSensor != null) {
            spinnerSensor.setAdapter(adapter);
            Log.d("ManualMeasurementActivity", "Spinner updated with " + sensorNames.size() + " sensors");

            spinnerSensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SoilManualInitFormResponse.SensorInfo selectedSensor = sensorList.get(position);
                    directMeasurementRequest.setNameSensor(selectedSensor.getNameSensor() != null ? selectedSensor.getNameSensor() : "N/A");
                    directMeasurementRequest.setCodeSensor(selectedSensor.getCodeSensor() != null ? selectedSensor.getCodeSensor() : "N/A");
                    directMeasurementRequest.setIdSensor(selectedSensor.getIdSensor());

                    String sensorInfo = "Tên cảm biến: " + directMeasurementRequest.getNameSensor() +
                            ", Mã cảm biến: " + directMeasurementRequest.getCodeSensor() +
                            ", ID: " + directMeasurementRequest.getIdSensor();
                    Log.d("ManualMeasurementActivity", "Đã chọn cảm biến - " + sensorInfo);

                    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                    String requestDetailDataJson = gson.toJson(directMeasurementRequest);
                    Log.d("ManualMeasurementActivity", "DirectMeasurementRequest: " + requestDetailDataJson);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("ManualMeasurementActivity", "Không có cảm biến nào được chọn");
                }
            });
        } else {
            Log.e("ManualMeasurementActivity", "Spinner is null");
            CustomToast.showCustomToast(this, "Không thể hiển thị danh sách cảm biến!");
        }
    }

    private void checkUsbDevices() {
        if (isMeasuring) return; // Không kiểm tra USB khi đang đo
        Map<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Log.d("ManualMeasurementActivity", "USB device list size: " + deviceList.size());
        if (deviceList.isEmpty()) {
            handler.post(() -> {
                usbStatusText.setText("Không tìm thấy thiết bị USB");
                readButton.setEnabled(false);
                findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                isDeviceConnected = false;
            });
            Toast.makeText(this, "No USB devices found", Toast.LENGTH_SHORT).show();
            return;
        }

        UsbDevice device = deviceList.values().iterator().next();
        Log.d("ManualMeasurementActivity", "Found USB device: " + device.getDeviceName());
        if (usbManager.hasPermission(device)) {
            Log.d("ManualMeasurementActivity", "Permission already granted for device: " + device.getDeviceName());
            setupSerialPort(device);
        } else if (!isPermissionRequestPending) {
            handler.post(() -> usbStatusText.setText("Đang yêu cầu quyền USB..."));
            Log.d("ManualMeasurementActivity", "Requesting USB permission for device: " + device.getDeviceName());
            Intent permissionIntent = new Intent(ACTION_USB_PERMISSION);
            permissionIntent.putExtra(UsbManager.EXTRA_DEVICE, device);
            PendingIntent pi = PendingIntent.getBroadcast(
                    this, 0, permissionIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );
            isPermissionRequestPending = true;
            usbManager.requestPermission(device, pi);

            handler.postDelayed(() -> {
                if (isPermissionRequestPending) {
                    Log.w("ManualMeasurementActivity", "No permission response received, retrying...");
                    isPermissionRequestPending = false;
                    checkUsbDevices();
                }
            }, 5000);
        }
    }

    private void setupSerialPort(UsbDevice device) {
        if (isMeasuring) return; // Không thiết lập lại cổng khi đang đo
        if (device == null) {
            handler.post(() -> {
                usbStatusText.setText("Không tìm thấy thiết bị USB để kết nối");
                readButton.setEnabled(false);
                findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                isDeviceConnected = false;
            });
            Log.e("ManualMeasurementActivity", "setupSerialPort: Device is null");
            Toast.makeText(this, "No USB device found to connect", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ManualMeasurementActivity", "Setting up serial port for device: " + device.getDeviceName());
        UsbSerialProber prober = UsbSerialProber.getDefaultProber();
        UsbSerialDriver driver = prober.probeDevice(device);
        if (driver == null) {
            handler.post(() -> {
                usbStatusText.setText("Thiết bị USB không được hỗ trợ");
                readButton.setEnabled(false);
                findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                isDeviceConnected = false;
            });
            Log.e("ManualMeasurementActivity", "No supported USB serial driver found for device: " + device.getDeviceName());
            Toast.makeText(this, "No supported USB serial device", Toast.LENGTH_SHORT).show();
            return;
        }

        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            handler.post(() -> {
                usbStatusText.setText("Không thể mở thiết bị USB");
                readButton.setEnabled(false);
                findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                isDeviceConnected = false;
            });
            Log.e("ManualMeasurementActivity", "Cannot open USB device: " + device.getDeviceName());
            Toast.makeText(this, "Cannot open USB device", Toast.LENGTH_SHORT).show();
            return;
        }

        usbSerialPort = driver.getPorts().get(0);
        try {
            usbSerialPort.open(connection);
            usbSerialPort.setParameters(9600, 8, 1, UsbSerialPort.PARITY_NONE);
            handler.post(() -> {
                usbStatusText.setText("Thiết bị USB đã sẵn sàng");
                readButton.setEnabled(true);
                findViewById(R.id.retry_button).setVisibility(View.GONE);
                isDeviceConnected = true;
            });
            Log.d("ManualMeasurementActivity", "USB serial port opened successfully for device: " + device.getDeviceName());
        } catch (Exception e) {
            handler.post(() -> {
                usbStatusText.setText("Lỗi mở cổng serial: " + e.getMessage());
                readButton.setEnabled(false);
                findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
                isDeviceConnected = false;
            });
            Log.e("ManualMeasurementActivity", "Failed to open serial port for device: " + device.getDeviceName() + ", Error: " + e.getMessage());
            Toast.makeText(this, "Failed to open serial port: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] calculateCrc(byte[] data) {
        int crc = 0xFFFF;
        for (byte pos : data) {
            crc ^= (pos & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return new byte[]{(byte) (crc & 0xFF), (byte) ((crc >> 8) & 0xFF)};
    }

    private byte[] sendModbusQuery(byte[] query) {
        if (!isDeviceConnected || usbSerialPort == null) {
            return null; // Thoát nếu thiết bị đã bị ngắt
        }

        byte[] crc = calculateCrc(query);
        byte[] queryWithCrc = new byte[query.length + crc.length];
        System.arraycopy(query, 0, queryWithCrc, 0, query.length);
        System.arraycopy(crc, 0, queryWithCrc, query.length, crc.length);

        try {
            usbSerialPort.write(queryWithCrc, 1000);
            Log.d("Modbus", "Sent query: " + Arrays.toString(queryWithCrc));
            byte[] response = new byte[9];
            int bytesRead = usbSerialPort.read(response, 1000);
            Log.d("Modbus", "Response received: " + Arrays.toString(response));
            if (bytesRead >= 7) {
                return response;
            }
        } catch (Exception e) {
            Log.e("Modbus", "Query failed: " + e.getMessage());
            handler.post(() -> {
                if (isMeasuring) {
                    usbStatusText.setText("Thiết bị USB đã bị ngắt kết nối");
                    readButton.setVisibility(View.VISIBLE);
                    isMeasuring = false;
                    isDeviceConnected = false;
                    Toast.makeText(ManualMeasurementActivity.this, "Thiết bị USB đã bị ngắt kết nối", Toast.LENGTH_LONG).show();
                }
            });
        }
        return null;
    }

    private void readSensorData() {
        if (usbSerialPort == null || !isDeviceConnected) {
            usbStatusText.setText("Thiết bị USB chưa sẵn sàng. Đang thử lại...");
            Toast.makeText(this, "USB device not initialized. Retrying...", Toast.LENGTH_SHORT).show();
            checkUsbDevices();
            return;
        }

        // Đặt cờ isMeasuring thành true khi bắt đầu đo
        isMeasuring = true;
        // Ẩn nút readButton khi bắt đầu đo
        handler.post(() -> readButton.setVisibility(View.GONE));

        executor.execute(() -> {
            List<Map<String, Object>> measurements = new ArrayList<>();
            final int TOTAL_MEASUREMENTS = 5;
            final int MEASUREMENTS_TO_AVERAGE = 3;
            final long DELAY_BETWEEN_MEASUREMENTS_MS = 10000;

            for (int i = 0; i < TOTAL_MEASUREMENTS; i++) {
                if (!isDeviceConnected || !isMeasuring) {
                    // Thoát vòng lặp nếu thiết bị bị ngắt kết nối
                    break;
                }

                // Hiển thị trạng thái đo
                final int measurementNumber = i + 1;
                handler.post(() -> usbStatusText.setText("Đang đo lần " + measurementNumber));

                Log.d("ManualMeasurementActivity", "Starting measurement " + measurementNumber);
                Map<String, Object> soilData = new HashMap<>();

                byte[] queryTempHumidity = new byte[]{0x01, 0x03, 0x00, 0x12, 0x00, 0x02};
                byte[] responseTemp = sendModbusQuery(queryTempHumidity);
                if (responseTemp != null && responseTemp.length >= 7) {
                    int humidity = ((responseTemp[3] & 0xFF) << 8) | (responseTemp[4] & 0xFF);
                    int temperature = ((responseTemp[5] & 0xFF) << 8) | (responseTemp[6] & 0xFF);
                    if ((temperature & 0x8000) != 0) {
                        temperature = -((~temperature & 0xFFFF) + 1);
                    }
                    soilData.put("humidity", (int) (humidity / 10.0));
                    soilData.put("temperature", (int) (temperature / 10.0));
                } else if (!isDeviceConnected) {
                    break;
                }

                byte[] queryEc = new byte[]{0x01, 0x03, 0x00, 0x15, 0x00, 0x01};
                byte[] responseEc = sendModbusQuery(queryEc);
                if (responseEc != null && responseEc.length >= 7) {
                    int ecValue = ((responseEc[3] & 0xFF) << 8) | (responseEc[4] & 0xFF);
                    soilData.put("electricalConductivity", ecValue);
                } else if (!isDeviceConnected) {
                    break;
                }

                byte[] queryPh = new byte[]{0x01, 0x03, 0x00, 0x06, 0x00, 0x01};
                byte[] responsePh = sendModbusQuery(queryPh);
                if (responsePh != null && responsePh.length >= 7) {
                    int phValue = ((responsePh[3] & 0xFF) << 8) | (responsePh[4] & 0xFF);
                    soilData.put("ph", (int) (phValue / 100.0));
                } else if (!isDeviceConnected) {
                    break;
                }

                byte[] queryNitrogen = new byte[]{0x01, 0x03, 0x00, 0x1E, 0x00, 0x01};
                byte[] responseNitrogen = sendModbusQuery(queryNitrogen);
                if (responseNitrogen != null && responseNitrogen.length >= 7) {
                    int nitrogenValue = ((responseNitrogen[3] & 0xFF) << 8) | (responseNitrogen[4] & 0xFF);
                    soilData.put("nitrogen", nitrogenValue);
                } else if (!isDeviceConnected) {
                    break;
                }

                byte[] queryPhosphorus = new byte[]{0x01, 0x03, 0x00, 0x1F, 0x00, 0x01};
                byte[] responsePhosphorus = sendModbusQuery(queryPhosphorus);
                if (responsePhosphorus != null && responsePhosphorus.length >= 7) {
                    int phosphorusValue = ((responsePhosphorus[3] & 0xFF) << 8) | (responsePhosphorus[4] & 0xFF);
                    soilData.put("phosphorus", phosphorusValue);
                } else if (!isDeviceConnected) {
                    break;
                }

                byte[] queryPotassium = new byte[]{0x01, 0x03, 0x00, 0x20, 0x00, 0x01};
                byte[] responsePotassium = sendModbusQuery(queryPotassium);
                if (responsePotassium != null && responsePotassium.length >= 7) {
                    int potassiumValue = ((responsePotassium[3] & 0xFF) << 8) | (responsePotassium[4] & 0xFF);
                    soilData.put("kalium", potassiumValue);
                } else if (!isDeviceConnected) {
                    break;
                }

                if (!soilData.isEmpty()) {
                    measurements.add(soilData);
                    Log.d("ManualMeasurementActivity", "Measurement " + measurementNumber + " added: " + soilData);
                } else {
                    Log.w("ManualMeasurementActivity", "Measurement " + measurementNumber + " failed, no valid data");
                }

                if (i < TOTAL_MEASUREMENTS - 1) {
                    try {
                        Thread.sleep(DELAY_BETWEEN_MEASUREMENTS_MS);
                    } catch (InterruptedException e) {
                        Log.e("ManualMeasurementActivity", "Measurement interrupted: " + e.getMessage());
                        handler.post(() -> Toast.makeText(ManualMeasurementActivity.this, "Đo bị gián đoạn", Toast.LENGTH_SHORT).show());
                        isMeasuring = false;
                        return;
                    }
                }
            }

            if (!isMeasuring) {
                return; // Thoát nếu đo bị dừng do thiết bị USB bị rút
            }

            if (measurements.size() < MEASUREMENTS_TO_AVERAGE) {
                handler.post(() -> {
                    Toast.makeText(ManualMeasurementActivity.this, "Không đủ dữ liệu đo (cần ít nhất " + MEASUREMENTS_TO_AVERAGE + " lần đo hợp lệ)", Toast.LENGTH_LONG).show();
                    usbStatusText.setText("Lỗi: Không đủ dữ liệu đo");
                    readButton.setVisibility(View.VISIBLE);
                    isMeasuring = false;
                });
                Log.e("ManualMeasurementActivity", "Not enough valid measurements: " + measurements.size());
                return;
            }

            List<Map<String, Object>> lastThreeMeasurements = measurements.subList(
                    Math.max(0, measurements.size() - MEASUREMENTS_TO_AVERAGE),
                    measurements.size()
            );

            Map<String, Double> averages = new HashMap<>();
            String[] keys = {"humidity", "temperature", "electricalConductivity", "ph", "nitrogen", "phosphorus", "kalium"};
            for (String key : keys) {
                double sum = 0.0;
                int count = 0;
                for (Map<String, Object> measurement : lastThreeMeasurements) {
                    if (measurement.containsKey(key)) {
                        sum += ((Number) measurement.get(key)).doubleValue();
                        count++;
                    }
                }
                averages.put(key, count > 0 ? sum / count : 0.0);
            }

            DirectMeasurementRequest.DataWriteSoilManual dataWriteSoilManual = new DirectMeasurementRequest.DataWriteSoilManual();
            dataWriteSoilManual.setHumidityName("Độ ẩm (% RH)");
            dataWriteSoilManual.setHumidity(averages.get("humidity").intValue());
            dataWriteSoilManual.setTemperatureName("Nhiệt độ (°C)");
            dataWriteSoilManual.setTemperature(averages.get("temperature").intValue());
            dataWriteSoilManual.setElectricalConductivityName("Độ dẫn điện EC (mS/m)");
            dataWriteSoilManual.setElectricalConductivity(averages.get("electricalConductivity").intValue());
            dataWriteSoilManual.setPhName("pH");
            dataWriteSoilManual.setPh(averages.get("ph").intValue());
            dataWriteSoilManual.setNitrogenName("Đạm (mg/kg)");
            dataWriteSoilManual.setNitrogen(averages.get("nitrogen").intValue());
            dataWriteSoilManual.setPhosphorusName("Lân (mg/kg)");
            dataWriteSoilManual.setPhosphorus(averages.get("phosphorus").intValue());
            dataWriteSoilManual.setKaliumName("Kali (mg/kg)");
            dataWriteSoilManual.setKalium(averages.get("kalium").intValue());
            directMeasurementRequest.setDataWriteSoilManual(dataWriteSoilManual);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String requestDetailDataJson = gson.toJson(directMeasurementRequest);
            Log.d("ManualMeasurementActivity", "DirectMeasurementRequest with Averaged Sensor Data: " + requestDetailDataJson);

            handler.post(() -> {
                // Update UI
                humidityText.setText("Humidity: " + (averages.get("humidity") != 0 ? averages.get("humidity").intValue() + " %" : "N/A"));
                temperatureText.setText("Temperature: " + (averages.get("temperature") != 0 ? averages.get("temperature").intValue() + " °C" : "N/A"));
                ecText.setText("EC: " + (averages.get("electricalConductivity") != 0 ? averages.get("electricalConductivity").intValue() + " µS/cm" : "N/A"));
                phText.setText("pH: " + (averages.get("ph") != 0 ? averages.get("ph").intValue() : "N/A"));
                nitrogenText.setText("Nitrogen: " + (averages.get("nitrogen") != 0 ? averages.get("nitrogen").intValue() + " mg/kg" : "N/A"));
                phosphorusText.setText("Phosphorus: " + (averages.get("phosphorus") != 0 ? averages.get("phosphorus").intValue() + " mg/kg" : "N/A"));
                potassiumText.setText("Potassium: " + (averages.get("kalium") != 0 ? averages.get("kalium").intValue() + " mg/kg" : "N/A"));
                usbStatusText.setText("Đo hoàn tất");

                readButton.setVisibility(View.VISIBLE);
                isMeasuring = false;

                // Navigate to LandInformationActivity
                Intent intent = new Intent(ManualMeasurementActivity.this, LandInformationActivity.class);
                intent.putExtra("directMeasurementRequest", directMeasurementRequest);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isMeasuring) {
            checkUsbDevices();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usbPermissionReceiver != null) {
            try {
                unregisterReceiver(usbPermissionReceiver);
            } catch (IllegalArgumentException e) {
                Log.e("ManualMeasurementActivity", "Error unregistering permission receiver: " + e.getMessage());
            }
        }
        if (usbDetachedReceiver != null) {
            try {
                unregisterReceiver(usbDetachedReceiver);
            } catch (IllegalArgumentException e) {
                Log.e("ManualMeasurementActivity", "Error unregistering detached receiver: " + e.getMessage());
            }
        }
        if (usbSerialPort != null) {
            try {
                usbSerialPort.close();
                Log.d("ManualMeasurementActivity", "USB serial port closed");
            } catch (Exception e) {
                Log.e("ManualMeasurementActivity", "Error closing serial port: " + e.getMessage());
            }
        }
        executor.shutdown();
    }
}