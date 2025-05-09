package com.example.appholaagri.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.DirectMeasurement.DirectMeasurementRequest;
import com.example.appholaagri.model.PlantInforByQrCode.PlantInforResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.model.SoilManualInitFormModel.SoilManualInitFormResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandInformationActivity extends AppCompatActivity {
    Button measure_again_button, send_info_button;
    private TextView sensorInfoText;
    private TextView measurementDataText;
    private TextView temperature_value, humidity_value, ec_value, nitrogen_value, phosphorus_value, potassium_value, ph_value;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private String token;
    private String qrCode;
    private DirectMeasurementRequest directMeasurementRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_land_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", null);

        // Khởi tạo giao diện
        send_info_button = findViewById(R.id.send_info_button);
        measure_again_button = findViewById(R.id.measure_again_button);
        temperature_value = findViewById(R.id.temperature_value);
        humidity_value = findViewById(R.id.humidity_value);
        ec_value = findViewById(R.id.ec_value);
        nitrogen_value = findViewById(R.id.nitrogen_value);
        phosphorus_value = findViewById(R.id.phosphorus_value);
        potassium_value = findViewById(R.id.potassium_value);
        ph_value = findViewById(R.id.ph_value);

        // Nhận DirectMeasurementRequest từ Intent
        directMeasurementRequest = (DirectMeasurementRequest) getIntent().getSerializableExtra("directMeasurementRequest");

        if (directMeasurementRequest != null && directMeasurementRequest.getDataWriteSoilManual() != null) {
            // Log đối tượng DirectMeasurementRequest
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String requestDetailDataJson = gson.toJson(directMeasurementRequest);
            Log.d("LandInformationActivity", "data nhan dc: " + requestDetailDataJson);

            // Lấy dữ liệu từ DataWriteSoilManual và gán vào TextView
            DirectMeasurementRequest.DataWriteSoilManual data = directMeasurementRequest.getDataWriteSoilManual();
            temperature_value.setText(String.valueOf(data.getTemperature()));
            humidity_value.setText(String.valueOf(data.getHumidity()));
            ec_value.setText(String.valueOf(data.getElectricalConductivity()));
            nitrogen_value.setText(String.valueOf(data.getNitrogen()));
            phosphorus_value.setText(String.valueOf(data.getPhosphorus()));
            potassium_value.setText(String.valueOf(data.getKalium()));
            ph_value.setText(String.valueOf(data.getPh()));
        } else {
            Toast.makeText(this, "Không nhận được dữ liệu cảm biến", Toast.LENGTH_SHORT).show();
            temperature_value.setText("N/A");
            humidity_value.setText("N/A");
            ec_value.setText("N/A");
            nitrogen_value.setText("N/A");
            phosphorus_value.setText("N/A");
            potassium_value.setText("N/A");
            ph_value.setText("N/A");
        }

        // Sự kiện cho nút "Đo lại"
        measure_again_button.setOnClickListener(view -> {
            Intent intent = new Intent(LandInformationActivity.this, ManualMeasurementActivity.class);
            startActivity(intent);
            finish();
        });

        // Sự kiện cho nút "Gửi thông tin"
        send_info_button.setOnClickListener(view -> {
            checkCameraPermission();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            qrCode = data.getStringExtra("QR_RESULT");
            Log.d("LandInformationActivity", "qrCode: " + qrCode);
            if (qrCode != null) {
                // Gửi dữ liệu lên server trước khi chuyển màn hình
                Intent intent = new Intent(LandInformationActivity.this, PlantInformationActivity.class);
                intent.putExtra("qrCode", qrCode);
                intent.putExtra("directMeasurementRequest", directMeasurementRequest); // Gửi DirectMeasurementRequest
                startActivity(intent);
                finish();
            }
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openQRScanner();
        } else {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showPermissionDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Quyền truy cập Camera")
                .setMessage("Vui lòng cho phép chúng tôi truy cập vào máy ảnh của bạn để tiếp tục.")
                .setPositiveButton("Cho phép", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openQRScanner() {
        Intent intent = new Intent(this, QRScannerActivity.class);
        intent.putExtra("SCAN_INSTRUCTION", "Quét mã qr ở cây gần nhất");
        startActivityForResult(intent, 200);
    }
}