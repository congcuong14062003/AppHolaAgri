package com.example.appholaagri.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.DayOverTimeAdapter;
import com.example.appholaagri.adapter.MeasurementLocationAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantRequest;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantResponse;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorRequest;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorResponse;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.PlantingDateModel.PlantingDateResponse;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.model.SensorAppInitFormModel.SensorAppInitFormResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationIdentifierSensorFragment extends BaseFragment {
    private int selectedPlantationId = -1;
    private int selectedAreaId = -1;
    private int selectedMonitoringId = -1;
    private int selectedAssetId = -1;

    private int isFixed = 0;
    private int selectedRow = -1;
    private int selectedColumn = -1;
    private String token;
    private String qrContent;
    private String qrContentIdentificationSensor;
    private Spinner spinnerPlantation, spinnerAsset;
    // đồn điền
    private List<SensorAppInitFormResponse.Plantation> plantationList = new ArrayList<>();
    private final List<String> plantationNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // tài sản
    private List<SensorAppInitFormResponse.Asset> assetList = new ArrayList<>();
    private final List<String> assetListName = new ArrayList<>();
    private ArrayAdapter<String> adapterAsset;


    private Spinner spinnerMonitoring, spinnerArea, spinnerRow, spinnerColumn;
    private List<SensorAppInitFormResponse.Monitoring> monitoringList = new ArrayList<>();
    private List<SensorAppInitFormResponse.Area> areaList = new ArrayList<>();


    private final List<String> monitoringNames = new ArrayList<>();
    private final List<String> areaNames = new ArrayList<>();
    private final List<String> rowNames = new ArrayList<>();
    private final List<String> columnNames = new ArrayList<>();

    private ArrayAdapter<String> adapterMonitoring;
    private ArrayAdapter<String> adapterArea;
    private ArrayAdapter<String> adapterRow;
    private ArrayAdapter<String> adapterColumn;


    private ApiInterface apiInterface;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_CODE_RESEND = 1001;

    private LinearLayout container_permanent;
    private AppCompatButton next_btn, add_measurementLocation_btn;


    // vị trí đo
    private MeasurementLocationAdapter measurementLocationAdapter;
    private List<IdentificationSensorRequest.MonitoringDetail> monitoringDetails = new ArrayList<>();
    private RecyclerView monitoringDetailsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_declaration_identifier_sensor, container, false);
        initViews(view);
        initApi();
        loadToken();
        getInitForSensor("");
        return view;
    }

    private void initViews(View view) {
        spinnerPlantation = view.findViewById(R.id.spinnerSensorPlantation);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, plantationNames);
        spinnerPlantation.setAdapter(adapter);
        spinnerPlantation.setOnItemSelectedListener(spinnerListener);


        spinnerAsset = view.findViewById(R.id.spinnerSensorAsset);
        adapterAsset = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, assetListName);
        spinnerAsset.setAdapter(adapterAsset);
        spinnerAsset.setOnItemSelectedListener(spinnerAssetListener);

        // điểm quan trắc
        spinnerMonitoring = view.findViewById(R.id.spinnerMonitoringPoint);
        adapterMonitoring = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, monitoringNames);
        spinnerMonitoring.setAdapter(adapterMonitoring);
        spinnerMonitoring.setOnItemSelectedListener(spinnerMonitoringListener);

        spinnerArea = view.findViewById(R.id.spinnerArea);
        adapterArea = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, areaNames);
        spinnerArea.setAdapter(adapterArea);
        spinnerArea.setOnItemSelectedListener(spinnerAreaListener);

        spinnerRow = view.findViewById(R.id.spinnerRow);
        adapterRow = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, rowNames);
        spinnerRow.setAdapter(adapterRow);
        spinnerRow.setOnItemSelectedListener(spinnerRowListener);

        spinnerColumn = view.findViewById(R.id.spinnerColumn);
        adapterColumn = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, columnNames);
        spinnerColumn.setAdapter(adapterColumn);
        spinnerColumn.setOnItemSelectedListener(spinnerColumnListener);

        // ánh xạ
        container_permanent = view.findViewById(R.id.container_permanent);
        next_btn = view.findViewById(R.id.next_btn);
        add_measurementLocation_btn = view.findViewById(R.id.add_measurementLocation_btn);
        container_permanent.setVisibility(View.GONE);


        // RecyclerView cho danh sách vị trí đo
        monitoringDetailsRecyclerView = view.findViewById(R.id.measurementLocationRecyclerView);
        measurementLocationAdapter = new MeasurementLocationAdapter(getContext(), monitoringDetails, areaList);
        monitoringDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        monitoringDetailsRecyclerView.setAdapter(measurementLocationAdapter);

        // Lắng nghe thay đổi từ Adapter
        measurementLocationAdapter.setOnMonitoringDetailsChangeListener(updatedList -> {
            monitoringDetails.clear();
            monitoringDetails.addAll(updatedList);
            measurementLocationAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
        });


        addLocation();

        // event
        add_measurementLocation_btn.setOnClickListener(view1 -> {
            addLocation();
        });
        if (isFixed == 1) {
            next_btn.setOnClickListener(view1 -> {
                if (isValidMonitoringDetails()) {
                    checkCameraPermissionSensor();
                } else {
                    CustomToast.showCustomToast(getContext(), "Vui lòng nhập đầy đủ thông tin phạm vi sử dụng");
                }
            });
        } else {
            next_btn.setOnClickListener(view1 -> {
                checkCameraPermissionSensor();
            });
        }


    }

    private boolean isValidMonitoringDetails() {
        for (IdentificationSensorRequest.MonitoringDetail detail : monitoringDetails) {
            if (detail.getIdCultivationArea() <= 0 ||
                    detail.getRowFrom() <= 0 ||
                    detail.getRowTo() <= 0 ||
                    detail.getColumnFrom() <= 0 ||
                    detail.getColumnTo() <= 0) {
                return false; // Nếu có bất kỳ giá trị nào không hợp lệ, trả về false
            }
        }
        return true; // Tất cả đều hợp lệ
    }

    private void initApi() {
        apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
    }

    private void loadToken() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", "");
    }

    private void addLocation() {
        monitoringDetails.add(new IdentificationSensorRequest.MonitoringDetail(0, 0, 0, 0, 0));
        measurementLocationAdapter.notifyItemInserted(monitoringDetails.size() - 1);
        measurementLocationAdapter.notifyDataSetChanged();  // Cập nhật toàn bộ RecyclerView
    }


    //    // sự kiện click của đồn điền
    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                SensorAppInitFormResponse.Plantation selectedPlantation = plantationList.get(position - 1);
                selectedPlantationId = selectedPlantation.getId(); // Lưu ID
                Log.d("DeclarationIdentifierSensorFragment", "Selected Plantation ID: " + selectedPlantationId);

                List<SensorAppInitFormResponse.Area> newAreaList = selectedPlantation.getArea();
                updateAreaSpinner(newAreaList);
                // Cập nhật danh sách khu vực trong Adapter
                measurementLocationAdapter.updateAreaList(newAreaList);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String newAreaLists = gson.toJson(newAreaList);
                Log.d("DeclarationIdentifierSensorFragment", "AreaList Adapter: " + newAreaLists);

            } else {
                selectedPlantationId = -1; // Reset ID khi chọn lại mặc định
                updateAreaSpinner(new ArrayList<>());
                measurementLocationAdapter.updateAreaList(new ArrayList<>());
            }
            checkEnableNextButton();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    // sự kiện click của tài sản
    private final AdapterView.OnItemSelectedListener spinnerAssetListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                SensorAppInitFormResponse.Asset selectedAsset = assetList.get(position - 1);
                selectedAssetId = selectedAsset.getId();
                isFixed = selectedAsset.getIsFixed(); // Giả sử có phương thức isFixed()
                Log.d("DeclarationIdentifierSensorFragment", "Selected Asset ID: " + selectedAssetId + ", isFixed: " + isFixed);
                if (isFixed == 1) {
                    checkCameraPermission();
                    container_permanent.setVisibility(View.VISIBLE);
                } else {
                    container_permanent.setVisibility(View.GONE);
                    checkEnableNextButton();
                }
            } else {
                selectedAssetId = -1; // Reset ID nếu chọn item mặc định
                checkEnableNextButton();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    // sự kiện click của khu vực
    private final AdapterView.OnItemSelectedListener spinnerAreaListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                SensorAppInitFormResponse.Area selectedArea = plantationList
                        .get(spinnerPlantation.getSelectedItemPosition() - 1)
                        .getArea().get(position - 1);

                selectedAreaId = selectedArea.getId();
                Log.d("DeclarationIdentifierSensorFragment", "Selected Area ID: " + selectedAreaId);
                updateMonitoringSpinner(selectedArea.getMonitoring());

                // Cập nhật số hàng và số cột
                updateRowAndColumn(selectedArea.getTotalRow(), selectedArea.getTotalColumn());
            } else {
                selectedAreaId = -1;
                updateMonitoringSpinner(new ArrayList<>());
                updateRowAndColumn(0, 0); // Reset hàng và cột
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // sự kiện click của điểm quan trắc
    private final AdapterView.OnItemSelectedListener spinnerMonitoringListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0 && position - 1 < monitoringList.size()) { // Kiểm tra danh sách trước khi truy cập
                SensorAppInitFormResponse.Monitoring selectedMonitoring = monitoringList.get(position - 1);
                selectedMonitoringId = selectedMonitoring.getId();
                // Tự động cập nhật hàng và cột
                // Tự động chọn hàng và cột
                int rowIn = selectedMonitoring.getRowIn();
                int columnIn = selectedMonitoring.getColumnIn();
                spinnerRow.setSelection(rowIn);
                spinnerColumn.setSelection(columnIn);
            } else {
                selectedMonitoringId = -1;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    // Gán sự kiện chọn hàng và cột
    // Sự kiện chọn hàng
    private final AdapterView.OnItemSelectedListener spinnerRowListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                selectedRow = Integer.parseInt(rowNames.get(position));
                filterMonitoringByRowAndColumn();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // Sự kiện chọn cột
    private final AdapterView.OnItemSelectedListener spinnerColumnListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                selectedColumn = Integer.parseInt(columnNames.get(position));
                filterMonitoringByRowAndColumn();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // Hàm lọc điểm quan trắc theo hàng và cột
    private void filterMonitoringByRowAndColumn() {
        if (selectedRow != -1 && selectedColumn != -1) {
            for (SensorAppInitFormResponse.Monitoring monitoring : monitoringList) {
                if (monitoring.getRowIn() == selectedRow && monitoring.getColumnIn() == selectedColumn) {
                    selectedMonitoringId = monitoring.getId();
                    spinnerMonitoring.setSelection(monitoringList.indexOf(monitoring) + 1);
                    Log.d("Monitoring Selection", "Updated selectedMonitoringId: " + selectedMonitoringId);
                    return;
                }
            }
        }
        selectedMonitoringId = -1; // Không tìm thấy
    }


    // Hàm kiểm tra điều kiện và bật/tắt button
    private void checkEnableNextButton() {
        if (selectedAssetId != -1 && selectedPlantationId != -1) {
            next_btn.setEnabled(true);
            next_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            next_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.secondarycolor)));
        } else {
            next_btn.setEnabled(false);
            next_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.secondarycolor));
            next_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e8f7f2")));
        }
    }


    private void getInitForSensor(String qrContent) {
        apiInterface.sensorInitForm(token, qrContent).enqueue(new Callback<ApiResponse<SensorAppInitFormResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SensorAppInitFormResponse>> call, Response<ApiResponse<SensorAppInitFormResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    ApiResponse<SensorAppInitFormResponse> apiResponse = response.body();
                    plantationList = apiResponse.getData().getPlantationList();
                    assetList = apiResponse.getData().getAssetList();
                    updateSpinnerData();
                    updateSpinnerAssetData();
                    // Tự động chọn các giá trị trả về từ API
                    autoFillData(apiResponse.getData());
                } else {
                    CustomToast.showCustomToast(getContext(), response.body() != null ? response.body().getMessage() : "Lỗi dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SensorAppInitFormResponse>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    // đồn điền
    private void updateSpinnerData() {
        plantationNames.clear();
        plantationNames.add("--Chọn đồn điền--");

        for (SensorAppInitFormResponse.Plantation plantation : plantationList) {
            plantationNames.add(plantation.getName());
        }
        adapter.notifyDataSetChanged();
    }

    // tài sản
    private void updateSpinnerAssetData() {
        assetListName.clear();
        assetListName.add("--Chọn tài sản--");

        for (SensorAppInitFormResponse.Asset asset : assetList) {
            assetListName.add(asset.getName());
        }
        adapterAsset.notifyDataSetChanged();
    }

    // khu vực
    private void updateAreaSpinner(List<SensorAppInitFormResponse.Area> areaList) {
        areaNames.clear();
        areaNames.add("--Chọn khu vực--"); // Item mặc định

        if (areaList != null && !areaList.isEmpty()) {
            for (SensorAppInitFormResponse.Area area : areaList) {
                areaNames.add(area.getName());
            }
        }

        adapterArea.notifyDataSetChanged();
        adapterMonitoring.notifyDataSetChanged();
        adapterRow.notifyDataSetChanged();
        adapterColumn.notifyDataSetChanged();
        spinnerArea.setSelection(0); // Reset về "--Chọn khu vực--"
    }

    // điểm quan trắc
    private void updateMonitoringSpinner(List<SensorAppInitFormResponse.Monitoring> newMonitoringList) {
        monitoringList.clear();  // Xóa dữ liệu cũ
        monitoringNames.clear();
        monitoringNames.add("--Chọn điểm quan trắc--"); // Item đầu tiên

        if (newMonitoringList != null && !newMonitoringList.isEmpty()) {
            monitoringList.addAll(newMonitoringList); // Thêm danh sách mới
            for (SensorAppInitFormResponse.Monitoring monitoring : newMonitoringList) {
                monitoringNames.add(monitoring.getName());
            }
        }

        Log.d("Monitoring Spinner", "Updated Monitoring List: " + newMonitoringList.size());

        adapterMonitoring.notifyDataSetChanged();
        spinnerMonitoring.setSelection(0);
    }

    // số hàng, cột
    private void updateRowAndColumn(int totalRow, int totalColumn) {
        rowNames.clear();
        columnNames.clear();

        rowNames.add("--Chọn hàng--");  // Item mặc định
        columnNames.add("--Chọn cột--");

        for (int i = 1; i <= totalRow; i++) {
            rowNames.add(String.valueOf(i));
        }
        for (int i = 1; i <= totalColumn; i++) {
            columnNames.add(String.valueOf(i));
        }

        adapterRow.notifyDataSetChanged();
        adapterColumn.notifyDataSetChanged();

        spinnerRow.setSelection(0);
        spinnerColumn.setSelection(0);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openQRScanner();
        } else {
            requestCameraPermission();
        }
    }

    private void checkCameraPermissionSensor() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openQRScannerSensor();
        } else {
            requestCameraPermission();
        }
    }

    private void openQRScanner() {
        Intent intent = new Intent(getContext(), QRScannerActivity.class);
        intent.putExtra("SCAN_INSTRUCTION", "Quét mã qr ở cây gần nhất"); // Truyền nội dung hướng dẫn
        startActivityForResult(intent, 200); // 200 là requestCode
    }

    private void openQRScannerSensor() {
        Intent intent = new Intent(getContext(), QRScannerSensor.class);
        intent.putExtra("SCAN_INSTRUCTION", "Quét mã qr ở cảm biến gần nhất"); // Truyền nội dung hướng dẫn
        startActivityForResult(intent, 200); // 200 là requestCode
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showPermissionDialog();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Quyền truy cập Camera")
                .setMessage("Vui lòng cho phép chúng tôi truy cập vào máy ảnh của bạn để tiếp tục.")
                .setPositiveButton("Cho phép", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getContext().getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            qrContent = data.getStringExtra("QR_RESULT");
            if (qrContent != null) {
                container_permanent.setVisibility(View.VISIBLE);
                Log.d("DeclarationIdentifierSensorFragment", "qrContent: " + qrContent);
                getInitForSensor(qrContent); // Gọi lại API với QR đã quét
            } else {
                container_permanent.setVisibility(View.VISIBLE);
            }
            qrContentIdentificationSensor = data.getStringExtra("QR_RESULT_SENSOR");
            if (qrContentIdentificationSensor != null) {
                handleIdentificationSensor(false);
            }
        }
        if (requestCode == 300 && resultCode == Activity.RESULT_OK && data != null) {
            boolean reload = data.getBooleanExtra("reload_fragment", false);
            if (reload) {
                reloadCurrentFragment(); // Làm mới chính Fragment này
            }
        }
    }


    private void autoFillData(SensorAppInitFormResponse data) {
        if (data == null || data.getObjectSelected() == null) return;
        SensorAppInitFormResponse.ObjectSelected objectSelected = data.getObjectSelected();
        int monitoringIndex = objectSelected.getMonitoringSelected();
        int areaIndex = objectSelected.getAreaSelected();
        int plantationIndex = objectSelected.getPlantationSelected();

        if (plantationIndex >= 0 && plantationIndex < plantationList.size()) {
            spinnerPlantation.setSelection(plantationIndex + 1);
        }

        spinnerPlantation.postDelayed(() -> {
            if (plantationIndex >= 0 && plantationIndex < plantationList.size()) {
                List<SensorAppInitFormResponse.Area> areas = plantationList.get(plantationIndex).getArea();
                updateAreaSpinner(areas);

                spinnerArea.postDelayed(() -> {
                    if (areaIndex >= 0 && areaIndex < areas.size()) {
                        spinnerArea.setSelection(areaIndex + 1);
                        SensorAppInitFormResponse.Area selectedArea = areas.get(areaIndex);

                        List<SensorAppInitFormResponse.Monitoring> monitoringList = selectedArea.getMonitoring();
                        updateMonitoringSpinner(monitoringList);

                        // Cập nhật số hàng và số cột trước khi chọn
                        updateRowAndColumn(selectedArea.getTotalRow(), selectedArea.getTotalColumn());

                        spinnerMonitoring.postDelayed(() -> {
                            if (monitoringIndex >= 0 && monitoringIndex < monitoringList.size()) {
                                spinnerMonitoring.setSelection(monitoringIndex + 1);
                                SensorAppInitFormResponse.Monitoring selectedMonitoring = monitoringList.get(monitoringIndex);

                                // Tự động chọn hàng và cột
                                int rowIn = selectedMonitoring.getRowIn();
                                int columnIn = selectedMonitoring.getColumnIn();

                                if (rowIn > 0 && rowIn <= selectedArea.getTotalRow()) {
                                    spinnerRow.setSelection(rowIn);
                                }

                                if (columnIn > 0 && columnIn <= selectedArea.getTotalColumn()) {
                                    spinnerColumn.setSelection(columnIn);
                                }

                                Log.d("AutoFill", "Chọn hàng: " + rowIn + ", cột: " + columnIn);
                                // **Chuyển đổi danh sách monitoringDetail**
                                List<SensorAppInitFormResponse.MonitoringDetail> monitoringDetailList = selectedMonitoring.getMonitoringDetail();
                                List<IdentificationSensorRequest.MonitoringDetail> convertedMonitoringDetails = new ArrayList<>();

                                if (monitoringDetailList != null && !monitoringDetailList.isEmpty()) {
                                    for (SensorAppInitFormResponse.MonitoringDetail detail : monitoringDetailList) {
                                        IdentificationSensorRequest.MonitoringDetail convertedDetail = new IdentificationSensorRequest.MonitoringDetail(
                                                detail.getColumnFrom(),
                                                detail.getColumnTo(),
                                                detail.getIdCultivationArea(),
                                                detail.getRowFrom(),
                                                detail.getRowTo()
                                        );
                                        convertedMonitoringDetails.add(convertedDetail);
                                    }
                                } else {
                                    // 🔴 Nếu không có dữ liệu, thêm một vị trí mới mặc định
                                    convertedMonitoringDetails.add(new IdentificationSensorRequest.MonitoringDetail(0, 0, 0, 0, 0));
                                }

                                // Cập nhật RecyclerView Adapter
                                measurementLocationAdapter.updateMonitoringDetails(convertedMonitoringDetails);

                            }
                        }, 100);
                    }
                }, 100);
            }
        }, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadFragment(); // Gọi hàm reload khi quay lại
    }

    private void reloadFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit(); // Làm mới chính Fragment hiện tại
    }

    private void handleIdentificationSensor(boolean isConfirmed) {
        IdentificationSensorRequest identificationSensorRequest = new IdentificationSensorRequest();
        identificationSensorRequest.setIdPlantation(selectedPlantationId);
        identificationSensorRequest.setIdCategoriesAsset(selectedAssetId);
        identificationSensorRequest.setIsConfirmed(isConfirmed ? 1 : 0); // Truyền giá trị isConfirmed
        identificationSensorRequest.setQrCode(qrContentIdentificationSensor);

        IdentificationSensorRequest.Monitoring monitoring = new IdentificationSensorRequest.Monitoring();
        monitoring.setIdMonitoring(selectedMonitoringId);
        monitoring.setIdCultivationArea(selectedAreaId);
        monitoring.setRowIn(selectedRow);
        monitoring.setColumnIn(selectedColumn);
        monitoring.setMonitoringDetail(monitoringDetails);

        identificationSensorRequest.setMonitoring(monitoring);

        logIdentificationSensorRequest(identificationSensorRequest);
        apiInterface.identificationSensor(token, identificationSensorRequest).enqueue(new Callback<ApiResponse<IdentificationSensorResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<IdentificationSensorResponse>> call, Response<ApiResponse<IdentificationSensorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 200) {
                        navigateToSuccessActivity();
                    } else if (status == 606) {
                        showConfirmationPopup(response.body().getMessage()); // Hiển thị popup khi status = 606
                    } else {
                        CustomToast.showCustomToast(getContext(), "Lỗi: " + response.body().getMessage());
//                        navigateToErrorActivity();
                    }
                } else {
                    CustomToast.showCustomToast(getContext(), response.body() != null ? response.body().getMessage() : "Lỗi dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<IdentificationSensorResponse>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    // Log JSON để kiểm tra dữ liệu truyền lên
    private void logIdentificationSensorRequest(IdentificationSensorRequest request) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(request);
        Log.d("DeclarationFragment", "IdentificationSensorRequest: " + json);
    }

    // Hiển thị popup xác nhận khi nhận status 606
    private void showConfirmationPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận cập nhật");
        builder.setMessage(message);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            // Gọi lại API với isConfirmed = 1
            handleIdentificationSensor(true);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Chuyển sang màn hình thành công
    private void navigateToSuccessActivity() {
        int tabIndex = getCurrentTabIndex(); // Lấy tab hiện tại
        Intent intent = new Intent(getContext(), SendSuccessData.class);
        intent.putExtra("tab_index", tabIndex); // Truyền tab_index vào Intent
        startActivity(intent);
    }


    private int getCurrentTabIndex() {
        DeclarationIdentifierActivity activity = (DeclarationIdentifierActivity) getActivity();
        if (activity != null) {
            return activity.getCurrentTabIndex();
        }
        return 0; // Mặc định là tab 0 (Cây trồng)
    }

    // 📌 Hàm để load lại chính Fragment
    private void reloadCurrentFragment() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit(); // Detach & Attach để load lại toàn bộ Fragment
    }
//    private void navigateToErrorActivity() {
//        Intent intent = new Intent(getContext(), SendFailedData.class);
//        startActivityForResult(intent, REQUEST_CODE_RESEND);
//    }
}
