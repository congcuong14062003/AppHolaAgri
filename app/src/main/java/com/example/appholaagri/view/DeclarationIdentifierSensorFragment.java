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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.DayOverTimeAdapter;
import com.example.appholaagri.adapter.MeasurementLocationAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorRequest;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.PlantingDateModel.PlantingDateResponse;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.model.SensorAppInitFormModel.SensorAppInitFormResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

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

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private String token;
    private String qrContent;
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
        adapterAsset = new ArrayAdapter<>(requireContext(),  android.R.layout.simple_spinner_dropdown_item, assetListName);
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

        monitoringDetailsRecyclerView = view.findViewById(R.id.measurementLocationRecyclerView);


        measurementLocationAdapter = new MeasurementLocationAdapter(monitoringDetails, new MeasurementLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                // Xử lý khi click vào item, có thể log ra hoặc cập nhật UI
            }
        });

        monitoringDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        monitoringDetailsRecyclerView.setAdapter(measurementLocationAdapter);

        addLocation();

        // event
        add_measurementLocation_btn.setOnClickListener(view1 -> {
            addLocation();
        });
    }

    private void initApi() {
        apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
    }

    private void loadToken() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", "");
    }

    private void addLocation() {
        // Thêm một đối tượng mới vào mảng listDayReqs
        monitoringDetails.add(new IdentificationSensorRequest.MonitoringDetail());
        // Cập nhật lại adapter của breakTimeRecycler
        measurementLocationAdapter.notifyItemInserted(monitoringDetails.size() - 1);
        // Cập nhật lại toàn bộ adapter của Day
        measurementLocationAdapter.notifyDataSetChanged();  // Đảm bảo cập nhật lại danh sách toàn bộ
    }


    //    // sự kiện click của đồn điền
    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                SensorAppInitFormResponse.Plantation selectedPlantation = plantationList.get(position - 1);
                selectedPlantationId = selectedPlantation.getId(); // Lưu ID
                Log.d("DeclarationIdentifierSensorFragment", "Selected Plantation ID: " + selectedPlantationId);
                updateAreaSpinner(selectedPlantation.getArea());
//                updateMeasurementLocationList(); // Gọi hàm cập nhật danh sách vị trí đo
            } else {
                selectedPlantationId = -1; // Reset ID khi chọn lại mặc định
                updateAreaSpinner(new ArrayList<>());
            }
            checkEnableNextButton();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };


    // sự kiện click của tài sản
    private final AdapterView.OnItemSelectedListener spinnerAssetListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) { // Bỏ qua item mặc định
                SensorAppInitFormResponse.Asset selectedAsset = assetList.get(position - 1);
                selectedAssetId = selectedAsset.getId();
                int isFixed = selectedAsset.getIsFixed(); // Giả sử có phương thức isFixed()
                Log.d("DeclarationIdentifierSensorFragment", "Selected Asset ID: " + selectedAssetId + ", isFixed: " + isFixed);
                if(isFixed == 1) {
                    checkCameraPermission();
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
        public void onNothingSelected(AdapterView<?> parent) {}
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
        public void onNothingSelected(AdapterView<?> parent) {}
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
        public void onNothingSelected(AdapterView<?> parent) {}
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
        public void onNothingSelected(AdapterView<?> parent) {}
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
        public void onNothingSelected(AdapterView<?> parent) {}
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

    private void openQRScanner() {
        Intent intent = new Intent(getContext(), QRScannerActivity.class);
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
                            }
                        }, 100);
                    }
                }, 100);
            }
        }, 100);
    }

}
