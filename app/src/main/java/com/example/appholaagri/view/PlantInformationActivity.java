package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.appholaagri.utils.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantInformationActivity extends AppCompatActivity {
    private String qrCode;
    private String token;
    private SoilManualInitFormResponse soilManualInitFormResponse;
    private DirectMeasurementRequest directMeasurementRequest;
    // UI components
    private TextView plantationText;
    private TextView plantAreaText;
    private TextView planTypeText;
    private TextView plandCodeText;
    private TextView plantingPeriodText;
    private Button backHomeButton;
    private Button sendInfoButton;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        plantationText = findViewById(R.id.plantation);
        plantAreaText = findViewById(R.id.plant_area);
        planTypeText = findViewById(R.id.plan_type);
        plandCodeText = findViewById(R.id.pland_code);
        plantingPeriodText = findViewById(R.id.planting_period);
        backHomeButton = findViewById(R.id.back_home_button);
        sendInfoButton = findViewById(R.id.send_info_button);
        loadingDialog = new LoadingDialog(this);

        // Get token and QR code
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", this.MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", null);
        soilManualInitFormResponse = new SoilManualInitFormResponse();
        Intent intent = getIntent();
        qrCode = intent.getStringExtra("qrCode");
        directMeasurementRequest = (DirectMeasurementRequest) intent.getSerializableExtra("directMeasurementRequest");

        // Set button listeners
        backHomeButton.setOnClickListener(v -> finish());

        sendInfoButton.setOnClickListener(v -> {
            handleSenDataToDirectMeasurement();
        });

        // Fetch plant data if QR code is available
        if (qrCode != null) {
            getDataPlant();
        }
    }

    public void getDataPlant() {
        loadingDialog.show();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<SoilManualInitFormResponse>> call = apiInterface.initFormPlantByQrCode(token, qrCode);
        call.enqueue(new Callback<ApiResponse<SoilManualInitFormResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<SoilManualInitFormResponse>> call, Response<ApiResponse<SoilManualInitFormResponse>> response) {
                loadingDialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SoilManualInitFormResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        soilManualInitFormResponse = apiResponse.getData();
                        updateUiPlant();
                        updateDirectMeasurementRequest();
                    } else {
                        Log.e("PlantInformationActivity", "API response unsuccessful: " + apiResponse.getMessage());
                        CustomToast.showCustomToast(PlantInformationActivity.this, "Lỗi: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e("PlantInformationActivity", "API response unsuccessful: " + response.message());
                    CustomToast.showCustomToast(PlantInformationActivity.this, "Lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SoilManualInitFormResponse>> call, Throwable t) {
                loadingDialog.hide();
                Log.e("PlantInformationActivity", "Error: " + t.getMessage());
                CustomToast.showCustomToast(PlantInformationActivity.this, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updateUiPlant() {
        if (soilManualInitFormResponse == null || soilManualInitFormResponse.getDataInfoPlant() == null) {
            CustomToast.showCustomToast(this, "Không có dữ liệu để hiển thị!");
            return;
        }

        SoilManualInitFormResponse.DataInfoPlant dataInfoPlant = soilManualInitFormResponse.getDataInfoPlant();

        // Update UI with API data
        plantationText.setText(dataInfoPlant.getNamePlantation() != null ? dataInfoPlant.getNamePlantation() : "N/A");
        plantAreaText.setText(dataInfoPlant.getNameCultivationArea() != null ? dataInfoPlant.getNameCultivationArea() : "N/A");
        planTypeText.setText(dataInfoPlant.getNameCropVarieties() != null ? dataInfoPlant.getNameCropVarieties() : "N/A");
        plandCodeText.setText(dataInfoPlant.getCodePlant() != null ? dataInfoPlant.getCodePlant() : "N/A");

        // Position: Row and Column
        String position = "Hàng " + dataInfoPlant.getRowIn() + ", Cột " + dataInfoPlant.getColumnIn();
        plantingPeriodText.setText(position);
    }

    private void updateDirectMeasurementRequest() {
        if (soilManualInitFormResponse == null || soilManualInitFormResponse.getDataInfoPlant() == null || directMeasurementRequest == null) {
            Log.e("PlantInformationActivity", "Không thể cập nhật DirectMeasurementRequest: Dữ liệu không đầy đủ");
            return;
        }

        SoilManualInitFormResponse.DataInfoPlant sourceData = soilManualInitFormResponse.getDataInfoPlant();
        DirectMeasurementRequest.DataInfoPlant dataInfoPlant = new DirectMeasurementRequest.DataInfoPlant();

        // Transfer data from SoilManualInitFormResponse.DataInfoPlant to DirectMeasurementRequest.DataInfoPlant
        dataInfoPlant.setIdCropVarieties(sourceData.getIdCropVarieties());
        dataInfoPlant.setNameCropVarieties(sourceData.getNameCropVarieties());
        dataInfoPlant.setNamePlantation(sourceData.getNamePlantation());
        dataInfoPlant.setCodePlant(sourceData.getCodePlant());
        dataInfoPlant.setRowIn(sourceData.getRowIn());
        dataInfoPlant.setIdPlantation(sourceData.getIdPlantation());
        dataInfoPlant.setIdCultivationArea(sourceData.getIdCultivationArea());
        dataInfoPlant.setIdPlant(sourceData.getIdPlant());
        dataInfoPlant.setColumnIn(sourceData.getColumnIn());
        dataInfoPlant.setNameCultivationArea(sourceData.getNameCultivationArea());

        // Set DataInfoPlant into DirectMeasurementRequest
        directMeasurementRequest.setDataInfoPlant(dataInfoPlant);
        directMeasurementRequest.setIsWrite(1);
    }

    public void handleSenDataToDirectMeasurement() {
        // Log the updated DirectMeasurementRequest
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String updatedRequestJson = gson.toJson(directMeasurementRequest);
        Log.d("PlantInformationActivity", "DirectMeasurementRequest: " + updatedRequestJson);

        if (directMeasurementRequest == null) {
            CustomToast.showCustomToast(this, "Không có dữ liệu cảm biến để gửi");
            return;
        }

        loadingDialog.show();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<String>> call = apiInterface.directMeasurement(token, directMeasurementRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                loadingDialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        // Create custom dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlantInformationActivity.this);
                        View dialogView = LayoutInflater.from(PlantInformationActivity.this).inflate(R.layout.dialog_success, null);
                        builder.setView(dialogView);

                        // Reference dialog buttons
                        Button btnOk = dialogView.findViewById(R.id.btn_continue);
                        Button btnCancel = dialogView.findViewById(R.id.btn_back_home);

                        AlertDialog dialog = builder.create();

                        // Handle OK button (Continue)
                        btnOk.setOnClickListener(v -> {
                            dialog.dismiss();
                            Intent intent = new Intent(PlantInformationActivity.this, ManualMeasurementActivity.class);
                            startActivity(intent);
                            finish();
                        });

                        // Handle Cancel button (Back to Home)
                        btnCancel.setOnClickListener(v -> {
                            dialog.dismiss();
                            Intent intent = new Intent(PlantInformationActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        });

                        dialog.setCancelable(false); // Prevent closing with back button
                        dialog.show();
                    } else {
                        Log.e("PlantInformationActivity", "API response unsuccessful: " + apiResponse.getMessage());
                        CustomToast.showCustomToast(PlantInformationActivity.this, "Gửi dữ liệu thất bại: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e("PlantInformationActivity", "API response unsuccessful: " + response.message());
                    CustomToast.showCustomToast(PlantInformationActivity.this, "Gửi dữ liệu thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                loadingDialog.hide();
                Log.e("PlantInformationActivity", "Error: " + t.getMessage());
                CustomToast.showCustomToast(PlantInformationActivity.this, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}