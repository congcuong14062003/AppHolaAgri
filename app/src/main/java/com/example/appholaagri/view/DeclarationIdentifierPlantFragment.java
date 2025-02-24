package com.example.appholaagri.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;

import android.Manifest;
import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ColAndRowNumberModel.ColAndRowNumberResponse;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.PlantingDateModel.PlantingDateResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationIdentifierPlantFragment extends BaseFragment {
    private Spinner spinnerPlantation;
    private List<PlanAppInitFormResponse.Plantation> plantationList;
    private ArrayAdapter<String> adapter;
    private List<String> plantationNames = new ArrayList<>();
    private CoordinatorLayout init_identify_plan_container;
    // area
    private Spinner spinnerPlantationArea;
    private ArrayAdapter<String> areaAdapter;
    private List<String> areaNames = new ArrayList<>();

    // loại cây
    private Spinner spinnerPlantationType;
    private List<PlanAppInitFormResponse.CropVariety> plantationType;
    private List<String> planType = new ArrayList<>();
    private ArrayAdapter<String> planTypeAdapter;

    private int selectedPlantationId = -1;
    private int selectedAreaId = -1;
    private int selectedCropVarietyId = -1;
    private int selectedPlantingDate = -1;

    // ngày trồng
    private Spinner spinnerPlantingDate;
    private List<PlantingDateResponse.DateModel> plantingDate;

    private ArrayAdapter<String> plantingDateAdapter;
    private List<String> plantingDateName = new ArrayList<>();

    private AppCompatButton next_btn;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_declaration_identifier_plant, container, false);
        spinnerPlantation = view.findViewById(R.id.spinnerPlantation);
        spinnerPlantationArea = view.findViewById(R.id.spinnerPlantationArea);
        spinnerPlantationType = view.findViewById(R.id.spinnerTypePlan);
        spinnerPlantingDate = view.findViewById(R.id.spinnerPlantingDay);
        init_identify_plan_container = view.findViewById(R.id.init_identify_plan_container);
        next_btn = view.findViewById(R.id.next_btn);
        // Khởi tạo Adapter cho Đồn Điền
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, plantationNames);
        spinnerPlantation.setAdapter(adapter);

        // Khởi tạo Adapter cho Khu Vực
        areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, areaNames);
        spinnerPlantationArea.setAdapter(areaAdapter);



        // Khởi tạo Adapter cho Loại Cây
        planTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, planType);
        spinnerPlantationType.setAdapter(planTypeAdapter);

        // khởi tạo adapter cho ngày trồng
        plantingDateName.add("--Chọn ngày trồng--");
        plantingDateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, plantingDateName);
        spinnerPlantingDate.setAdapter(plantingDateAdapter);


        // Sự kiện chọn Đồn Điền
        spinnerPlantation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Bỏ qua item mặc định
                    PlanAppInitFormResponse.Plantation selectedPlantation = plantationList.get(position - 1);
                    selectedPlantationId = selectedPlantation.getId(); // Lưu ID
                    Log.d("SpinnerSelection", "Selected Plantation ID: " + selectedPlantationId);
                    updateAreaSpinner(selectedPlantation.getArea());
                } else {
                    selectedPlantationId = -1; // Reset ID khi chọn lại mặc định
                    updateAreaSpinner(new ArrayList<>());
                }
                checkAndFetchPlantingDates();
                checkFullInfor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerPlantationArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PlanAppInitFormResponse.Area selectedArea = plantationList.get(spinnerPlantation.getSelectedItemPosition() - 1)
                            .getArea().get(position - 1);
                    selectedAreaId = selectedArea.getId(); // Lưu ID
                    Log.d("SpinnerSelection", "Selected Area ID: " + selectedAreaId);
                } else {
                    selectedAreaId = -1; // Reset ID khi chọn lại mặc định
                }
                checkAndFetchPlantingDates();
                checkFullInfor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        spinnerPlantationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PlanAppInitFormResponse.CropVariety selectedType = plantationType.get(position - 1);
                    selectedCropVarietyId = selectedType.getId(); // Lưu ID
                    Log.d("SpinnerSelection", "Selected Crop Variety ID: " + selectedCropVarietyId);
                } else {
                    selectedCropVarietyId = -1; // Reset ID khi chọn lại mặc định
                }
                checkAndFetchPlantingDates();
                checkFullInfor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý chọn ngày từ Spinner
        spinnerPlantingDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Bỏ qua item mặc định
                    PlantingDateResponse.DateModel selectedPlantingDay = plantingDate.get(position - 1);
                    selectedPlantingDate = selectedPlantingDay.getId(); // Lưu ID
                    Log.d("SpinnerSelection", "Selected Plantation ID: " + selectedPlantingDate);
                } else {
                    selectedPlantingDate = -1; // Reset ID khi chọn lại mặc định
                    updateSpinnerPlantingDate();
                }
                checkFullInfor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Gọi API lấy dữ liệu
        init_identify_plan_container.setVisibility(View.GONE);

        next_btn.setOnClickListener(view1 -> checkCameraPermission());
        getInitFormPlan();
        return view;
    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openQRScanner();
        } else {
            requestCameraPermission();
        }
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
    private void openQRScanner() {
        Intent intent = new Intent(getContext(), QRScannerActivity.class);
        startActivityForResult(intent, 200); // 200 là requestCode
    }


    private void checkAndFetchPlantingDates() {
        if (selectedPlantationId != -1 && selectedAreaId != -1 && selectedCropVarietyId != -1) {
            Log.d("API Call", "Fetching planting dates with IDs: Plantation=" + selectedPlantationId +
                    ", Area=" + selectedAreaId + ", CropVariety=" + selectedCropVarietyId);
            getInitFormPlanDate(selectedCropVarietyId, selectedAreaId, selectedPlantationId);
        }
    }

    private void checkFullInfor() {
        if (selectedPlantationId != -1 && selectedAreaId != -1 && selectedCropVarietyId != -1 && selectedPlantingDate != -1) {
            next_btn.setEnabled(true);
            next_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            next_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.secondarycolor)));
        } else {
            next_btn.setEnabled(false);
            next_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.secondarycolor));
            next_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e8f7f2")));
        }
    }


    private void getInitFormPlan() {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<PlanAppInitFormResponse>> call = apiInterface.planInitForm(token);

        call.enqueue(new Callback<ApiResponse<PlanAppInitFormResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PlanAppInitFormResponse>> call, Response<ApiResponse<PlanAppInitFormResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PlanAppInitFormResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        plantationList = apiResponse.getData().getPlantationList();
                        plantationType = apiResponse.getData().getCropVarietiesList();
                        updateSpinnerData();
                        updatePlantationTypeSpinner();
                        init_identify_plan_container.setVisibility(View.VISIBLE);
                    } else {
                        CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
                    }
                } else {
                    Log.e("DeclarationIdentifierPlantFragment", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PlanAppInitFormResponse>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void getInitFormPlanDate(int idCropVarieties,int idCultivationArea,int idPlantation) {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<PlantingDateResponse>> call = apiInterface.planInitFormDate(token, idCropVarieties, idCultivationArea, idPlantation);

        call.enqueue(new Callback<ApiResponse<PlantingDateResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PlantingDateResponse>> call, Response<ApiResponse<PlantingDateResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PlantingDateResponse> apiResponse = response.body();
                    // Chuyển đổi đối tượng thành JSON để debug
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonResponse = gson.toJson(apiResponse);
                    Log.d("API Response", "data ngay trong: " + jsonResponse);
                    if (apiResponse.getStatus() == 200) {
                        if (apiResponse.getData() != null && apiResponse.getData().getDateList() != null) {
                            plantingDate = apiResponse.getData().getDateList();
                            updateSpinnerPlantingDate();
                        } else {
                            CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
                            plantingDate = new ArrayList<>(); // Tránh lỗi null
                            updateSpinnerPlantingDate();
                        }

                    } else {
                        CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
                    }
                } else {
                    Log.e("DeclarationIdentifierPlantFragment", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PlantingDateResponse>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }
    private void updateSpinnerData() {
        plantationNames.clear();
        plantationNames.add("--Chọn đồn điền--"); // Thêm item mặc định

        if (plantationList != null) {
            for (PlanAppInitFormResponse.Plantation plantation : plantationList) {
                plantationNames.add(plantation.getName());
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void updatePlantationTypeSpinner() {
        planType.clear(); // Xóa dữ liệu cũ
        planType.add("--Chọn loại cây--"); // Thêm item mặc định

        if (plantationType != null && !plantationType.isEmpty()) {
            for (PlanAppInitFormResponse.CropVariety type : plantationType) {
                planType.add(type.getName());
            }
        } else {
            Log.e("updatePlantationTypeSpinner", "Danh sách loại cây trống hoặc null");
        }

        planTypeAdapter.notifyDataSetChanged(); // Cập nhật Adapter
        spinnerPlantationType.setSelection(0); // Đặt lại về mặc định
    }

    private void updateAreaSpinner(List<PlanAppInitFormResponse.Area> areaList) {
        areaNames.clear();
        areaNames.add("--Chọn khu vực--"); // Item mặc định

        if (areaList != null && !areaList.isEmpty()) {
            for (PlanAppInitFormResponse.Area area : areaList) {
                areaNames.add(area.getName());
            }
        }

        areaAdapter.notifyDataSetChanged();
        spinnerPlantationArea.setSelection(0); // Reset về "--Chọn khu vực--"
    }

    private void updateSpinnerPlantingDate() {
        plantingDateName.clear();
        plantingDateName.add("--Chọn ngày trồng--"); // Thêm item mặc định

        if (plantingDate != null && !plantingDate.isEmpty()) {
            for (PlantingDateResponse.DateModel dateModel : plantingDate) {
                plantingDateName.add(dateModel.getName());
            }
        }
        plantingDateAdapter.notifyDataSetChanged();
        spinnerPlantingDate.setSelection(0); // Đặt về mặc định
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            String qrContent = data.getStringExtra("QR_RESULT");
            Log.d("QRScanner", "Dữ liệu QR: " + qrContent);
            CustomToast.showCustomToast(getContext(), "QR Code: " + qrContent);
            // Thực hiện hành động tiếp theo với dữ liệu QR (gửi API, cập nhật UI,...)
            if(qrContent != null) {
                showSelectRowColumnDialog(qrContent);
            }
        }
    }
    private void showSelectRowColumnDialog(String qrContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_row_column, null);
        builder.setView(dialogView);

        NumberPicker npRow = dialogView.findViewById(R.id.npRow);
        NumberPicker npColumn = dialogView.findViewById(R.id.npColumn);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        npRow.setWrapSelectorWheel(true);
        npColumn.setWrapSelectorWheel(true);

        Log.d("cuong", "selectedAreaId: " + selectedAreaId);
        Log.d("cuong", "qrContent: " + qrContent);

        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Gọi API lấy danh sách hàng
        Call<ApiResponse<List<ColAndRowNumberResponse>>> callRow = apiInterface.planRowFormFormDate(token, selectedAreaId, qrContent);
        callRow.enqueue(new Callback<ApiResponse<List<ColAndRowNumberResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Response<ApiResponse<List<ColAndRowNumberResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ColAndRowNumberResponse>> apiResponse = response.body();
                    List<ColAndRowNumberResponse> rowData = apiResponse.getData();

                    if (rowData != null && !rowData.isEmpty()) {
                        int[] rowIds = new int[rowData.size()];
                        for (int i = 0; i < rowData.size(); i++) {
                            rowIds[i] = rowData.get(i).getId();
                        }

                        npRow.setMinValue(0);
                        npRow.setMaxValue(rowData.size() - 1);

                        String[] idStrings = new String[rowData.size()];
                        for (int i = 0; i < rowData.size(); i++) {
                            idStrings[i] = String.valueOf(rowIds[i]);
                        }
                        npRow.setDisplayedValues(idStrings);

                        // Chọn ID mặc định của hàng đầu tiên để gọi danh sách cột
                        int defaultRowId = rowIds[0];
                        fetchColumnData(apiInterface, token, defaultRowId, npColumn, qrContent);

                        // Khi thay đổi hàng, gọi lại API lấy cột
                        npRow.setOnValueChangedListener((picker, oldVal, newVal) -> {
                            int selectedRowId = rowIds[newVal];
                            Log.d("NumberPicker", "Chọn hàng ID: " + selectedRowId);
                            fetchColumnData(apiInterface, token, selectedRowId, npColumn, qrContent);
                        });
                    } else {
                        Log.e("API Error", "Danh sách hàng trống");
                    }
                } else {
                    Log.e("API Error", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnConfirm.setOnClickListener(v -> {
            int selectedRowIndex = npRow.getValue();
            int selectedColumnIndex = npColumn.getValue();
            Log.d("RowColumn", "Hàng ID đã chọn: " + selectedRowIndex + ", Cột ID đã chọn: " + selectedColumnIndex);
            dialog.dismiss();
        });

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    private void fetchColumnData(ApiInterface apiInterface, String token, int rowId, NumberPicker npColumn, String qrContent) {

//        Log.d("cuong1", "selectedAreaId: " + selectedAreaId);
//        Log.d("cuong1", "qrContent: " + qrContent);
//        Log.d("cuong1", "rowId: " + rowId);
        Log.d("cuong1", "re-render: " + rowId);

        Call<ApiResponse<List<ColAndRowNumberResponse>>> callColumn = apiInterface.planColumnFormFormDate(token, selectedAreaId, qrContent, rowId);
        callColumn.enqueue(new Callback<ApiResponse<List<ColAndRowNumberResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Response<ApiResponse<List<ColAndRowNumberResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ColAndRowNumberResponse>> apiResponse = response.body();
                    List<ColAndRowNumberResponse> columnData = apiResponse.getData();

                    if (columnData != null && !columnData.isEmpty()) {
                        int[] columnIds = new int[columnData.size()];
                        for (int i = 0; i < columnData.size(); i++) {
                            columnIds[i] = columnData.get(i).getId();
                        }

                        npColumn.setMinValue(0);
                        npColumn.setMaxValue(columnData.size() - 1);

                        String[] columnStrings = new String[columnData.size()];
                        for (int i = 0; i < columnData.size(); i++) {
                            columnStrings[i] = String.valueOf(columnIds[i]);
                        }
                        npColumn.setDisplayedValues(columnStrings);

                        Log.d("API Column", "Đã cập nhật danh sách cột theo hàng ID: " + rowId);
                    } else {
                        Log.e("API Error", "Danh sách cột trống");
                    }
                } else {
                    Log.e("API Error", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

}
