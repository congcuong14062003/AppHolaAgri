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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.content.SharedPreferences;

import android.Manifest;
import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ColAndRowNumberModel.ColAndRowNumberResponse;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantRequest;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantResponse;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.PlantingDateModel.PlantingDateResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.service.ResendRequestCallback;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationIdentifierPlantFragment extends BaseFragment implements ResendRequestCallback {
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
    private String selectedPlantingDateName;

    private static final int REQUEST_CODE_RESEND = 1001;

    private String token;
    private String qrContent;
    private int selectedRow = -1;
    private int selectedCol = -1;


    // ngày trồng
    private Spinner spinnerPlantingDate;
    private List<PlantingDateResponse.DateModel> plantingDate;

    private ArrayAdapter<String> plantingDateAdapter;
    private List<String> plantingDateName = new ArrayList<>();

    private AppCompatButton next_btn;
    private static final int CAMERA_PERMISSION_CODE = 100;
    ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);

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


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", null);

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
                    selectedPlantingDateName = selectedPlantingDay.getName();
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
        Call<ApiResponse<PlantingDateResponse>> call = apiInterface.planInitFormDate(token, idCropVarieties, idCultivationArea, idPlantation);
        call.enqueue(new Callback<ApiResponse<PlantingDateResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PlantingDateResponse>> call, Response<ApiResponse<PlantingDateResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PlantingDateResponse> apiResponse = response.body();
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
            qrContent = data.getStringExtra("QR_RESULT");
            // Thực hiện hành động tiếp theo với dữ liệu QR (gửi API, cập nhật UI,...)
            if(qrContent != null) {
                showSelectRowColumnDialog(qrContent);
            }
        }
        if (requestCode == REQUEST_CODE_RESEND && resultCode == Activity.RESULT_OK) {
            // Gọi lại API khi người dùng nhấn "Gửi lại"
            handleIdentificationPlant(token, selectedRow, selectedCol, qrContent);
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

        Call<ApiResponse<List<ColAndRowNumberResponse>>> callRow = apiInterface.planRowFormFormDate(token, selectedAreaId, qrContent);
        callRow.enqueue(new Callback<ApiResponse<List<ColAndRowNumberResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Response<ApiResponse<List<ColAndRowNumberResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ColAndRowNumberResponse> rowData = response.body().getData();

                    if (rowData != null && !rowData.isEmpty()) {
                        int[] rowIds = new int[rowData.size()];
                        String[] idStrings = new String[rowData.size()];

                        for (int i = 0; i < rowData.size(); i++) {
                            rowIds[i] = rowData.get(i).getId();
                            idStrings[i] = String.valueOf(rowIds[i]);
                        }

                        npRow.setMinValue(0);
                        npRow.setMaxValue(rowData.size() - 1);
                        npRow.setDisplayedValues(idStrings);
                        npRow.setValue(0); // Chọn mặc định

                        int defaultRowId = rowIds[0];
                        fetchColumnData(token, defaultRowId, npColumn, qrContent);

                        npRow.setOnValueChangedListener((picker, oldVal, newVal) -> {
                            fetchColumnData(token, rowIds[newVal], npColumn, qrContent);
                        });
                    }
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
            selectedRow = Integer.parseInt(npRow.getDisplayedValues()[npRow.getValue()]);
            selectedCol = Integer.parseInt(npColumn.getDisplayedValues()[npColumn.getValue()]);
            handleIdentificationPlant(token, selectedRow, selectedCol, qrContent);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void fetchColumnData(String token, int rowId, NumberPicker npColumn, String qrContent) {
        Call<ApiResponse<List<ColAndRowNumberResponse>>> callColumn = apiInterface.planColumnFormFormDate(token, selectedAreaId, qrContent, rowId);
        callColumn.enqueue(new Callback<ApiResponse<List<ColAndRowNumberResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ColAndRowNumberResponse>>> call, Response<ApiResponse<List<ColAndRowNumberResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ColAndRowNumberResponse>> apiResponse = response.body();
                    List<ColAndRowNumberResponse> columnData = apiResponse.getData();

                    // Reset NumberPicker để tránh lỗi chỉ mục
                    npColumn.setDisplayedValues(null);
                    npColumn.setMinValue(0);
                    npColumn.setMaxValue(0);

                    if (columnData != null && !columnData.isEmpty()) {
                        int columnSize = columnData.size();
                        int[] columnIds = new int[columnSize];

                        for (int i = 0; i < columnSize; i++) {
                            columnIds[i] = columnData.get(i).getId();
                        }

                        npColumn.setMinValue(0);
                        npColumn.setMaxValue(columnSize - 1);

                        String[] columnStrings = new String[columnSize];
                        for (int i = 0; i < columnSize; i++) {
                            columnStrings[i] = String.valueOf(columnIds[i]);
                        }

                        npColumn.setDisplayedValues(columnStrings);

                        Log.d("API Column", "Đã cập nhật danh sách cột theo hàng ID: " + rowId);
                    } else {
                        Log.e("API Error", "Danh sách cột trống");

                        // Khi không có dữ liệu, đặt giá trị an toàn
                        npColumn.setDisplayedValues(new String[]{"Không có dữ liệu"});
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


    private void handleIdentificationPlant(String token, int selectedRow, int selectedCol, String qrContent) {
        // Tạo đối tượng DateSelected
        IdentificationPlantRequest.DateSelected dateSelected = new IdentificationPlantRequest.DateSelected(selectedPlantingDate, selectedPlantingDateName);
        // Khởi tạo request với dữ liệu
        IdentificationPlantRequest identificationPlantRequest = new IdentificationPlantRequest(
                selectedCol,         // columnIn
                dateSelected,        // dateSelected
                selectedCropVarietyId, // idCropVarieties
                selectedAreaId,       // idCultivationArea
                selectedPlantationId, // idPlantation
                0,                   // isConfirmed (1: đã xác nhận, 0: chưa xác nhận)
                qrContent,           // qrCode
                selectedRow          // rowIn
        );

        Call<ApiResponse<IdentificationPlantResponse>> call = apiInterface.identificationPlant(token, identificationPlantRequest);
        call.enqueue(new Callback<ApiResponse<IdentificationPlantResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<IdentificationPlantResponse>> call, Response<ApiResponse<IdentificationPlantResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<IdentificationPlantResponse> apiResponse = response.body();

                    // Debug log response
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("DeclarationIdentifierPlantFragment", "data xác định cây: " + jsonResponse);
                    Log.d("DeclarationIdentifierPlantFragment", "selectedCol: " + selectedCol);
                    Log.d("DeclarationIdentifierPlantFragment", "dateSelected: " + dateSelected);
                    Log.d("DeclarationIdentifierPlantFragment", "selectedCropVarietyId: " + selectedCropVarietyId);
                    Log.d("DeclarationIdentifierPlantFragment", "selectedAreaId: " + selectedAreaId);
                    Log.d("DeclarationIdentifierPlantFragment", "selectedPlantationId: " + selectedPlantationId);
                    Log.d("DeclarationIdentifierPlantFragment", "qrContent: " + qrContent);
                    Log.d("DeclarationIdentifierPlantFragment", "selectedRow: " + selectedRow);

                    if (apiResponse.getStatus() == 200) {
                        navigateToSuccessActivity();
                    } else if (apiResponse.getStatus() == 606) {
                        showConfirmationPopup(token, selectedRow, selectedCol, qrContent, apiResponse.getMessage());
                    } else {
                        CustomToast.showCustomToast(getContext(), "Lỗi: " + apiResponse.getMessage());

                        navigateToErrorActivity();
                    }
                } else {
                    CustomToast.showCustomToast(getContext(), "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<IdentificationPlantResponse>> call, Throwable t) {
                CustomToast.showCustomToast(getContext(), "Lỗi: " + t.getMessage());
            }
        });
    }

    // Chuyển sang màn hình thành công
    private void navigateToSuccessActivity() {
        Intent intent = new Intent(getContext(), SendSuccessData.class);
        startActivity(intent);
    }



    // Hiển thị popup xác nhận khi nhận status 606
    private void showConfirmationPopup(String token, int selectedRow, int selectedCol, String qrContent, String message) {
        Log.d("DeclarationIdentifierPlantFragment", "Dữ liệu thông tin selectedRow: " + selectedRow);
        Log.d("DeclarationIdentifierPlantFragment", "Dữ liệu thông tin selectedCol: " + selectedCol);
        Log.d("DeclarationIdentifierPlantFragment", "Dữ liệu thông tin qrContent: " + qrContent);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận cập nhật");
        builder.setMessage(message);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            // Gọi lại API với isConfirmed = 1
            IdentificationPlantRequest.DateSelected dateSelected = new IdentificationPlantRequest.DateSelected(selectedPlantingDate, selectedPlantingDateName);
            IdentificationPlantRequest identificationPlantRequest = new IdentificationPlantRequest(
                    selectedCol,
                    dateSelected,
                    selectedCropVarietyId,
                    selectedAreaId,
                    selectedPlantationId,
                    1,  // isConfirmed = 1
                    qrContent,
                    selectedRow
            );

            Call<ApiResponse<IdentificationPlantResponse>> call = apiInterface.identificationPlant(token, identificationPlantRequest);
            call.enqueue(new Callback<ApiResponse<IdentificationPlantResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<IdentificationPlantResponse>> call, Response<ApiResponse<IdentificationPlantResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<IdentificationPlantResponse> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            navigateToSuccessActivity();
                        } else {
                            CustomToast.showCustomToast(getContext(), "Lỗi: " + apiResponse.getMessage());
                            navigateToErrorActivity();
                        }
                    } else {
                        CustomToast.showCustomToast(getContext(), "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<IdentificationPlantResponse>> call, Throwable t) {
                    CustomToast.showCustomToast(getContext(), "Lỗi: " + t.getMessage());
                }
            });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void navigateToErrorActivity() {
        Intent intent = new Intent(getContext(), SendFailedData.class);
        startActivityForResult(intent, REQUEST_CODE_RESEND);
    }


    @Override
    public void onResendRequest() {

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}
