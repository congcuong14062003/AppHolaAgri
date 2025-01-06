package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.CheckInQrCodeModel.CheckInQrCodeRequest;
import com.example.appholaagri.model.ShiftModel.ShiftModel;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInDailyActivity extends AppCompatActivity {

    private RadioGroup radioGroupShift;
    private List<ShiftModel.Shift> workShiftList = new ArrayList<>();
    private int selectedShiftId = -1;
    private ApiInterface apiInterface;
    ImageView close_scanner;
    private DecoratedBarcodeView barcodeScannerView;  // Change to DecoratedBarcodeView
    private CaptureManager captureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_daily);

        // Initialize views
        radioGroupShift = findViewById(R.id.radioGroupShift);
        barcodeScannerView = findViewById(R.id.barcode_scanner);
        close_scanner = findViewById(R.id.close_scanner);
        barcodeScannerView.setStatusText("");  // Đặt thông báo là chuỗi rỗng

        // Initialize API interface
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        close_scanner.setOnClickListener(view -> {
            onBackPressed();
        });

        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("CheckInDailyActivity", "Token: " + token);
        if (token != null) {
            // Fetch shift list from API
            getShiftList(token);
        } else {
            Toast.makeText(this, "Token không tồn tại", Toast.LENGTH_SHORT).show();
        }

        // Initialize the barcode scanner
        captureManager = new CaptureManager(this, barcodeScannerView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

        barcodeScannerView.decodeSingle(result -> {
            if (selectedShiftId == -1) {  // Kiểm tra xem ca đã được chọn chưa
                Toast.makeText(CheckInDailyActivity.this, "Vui lòng chọn ca trước khi quét QR", Toast.LENGTH_SHORT).show();
            } else {
                if (result != null) {
                    Toast.makeText(CheckInDailyActivity.this, "QR Code: " + result.getText(), Toast.LENGTH_SHORT).show();
                    Log.d("CheckInDailyActivity", "QR Code: " + result.getText());
                    String qrCodeString = result.getText();
                    checkInQrCode(qrCodeString);
                } else {
                    Toast.makeText(CheckInDailyActivity.this, "Quét QR thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(CheckInDailyActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
    private void getShiftList(String token) {
        // Hiển thị ProgressBar khi tải dữ liệu

        // Gọi API để lấy danh sách ca làm việc
        Call<ApiResponse<List<ShiftModel.Shift>>> call = apiInterface.shiftModel(token);
        call.enqueue(new Callback<ApiResponse<List<ShiftModel.Shift>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ShiftModel.Shift>>> call, Response<ApiResponse<List<ShiftModel.Shift>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ShiftModel.Shift>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        workShiftList = apiResponse.getData();
                        updateRadioGroup();
                        radioGroupShift.setVisibility(View.VISIBLE); // Hiển thị RadioGroup
                    }
                } else {
                    Toast.makeText(CheckInDailyActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShiftModel.Shift>>> call, Throwable t) {
                Toast.makeText(CheckInDailyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRadioGroup() {
        radioGroupShift.removeAllViews(); // Clear existing views
        for (ShiftModel.Shift shift : workShiftList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(shift.getDisplayName());
            radioButton.setId(shift.getWorkShiftId());
            radioButton.setTextColor(getResources().getColor(android.R.color.white));
            radioButton.setTextSize(16);
            radioGroupShift.addView(radioButton);
        }

        // Lắng nghe khi người dùng chọn ca
        radioGroupShift.setOnCheckedChangeListener((group, checkedId) -> {
            selectedShiftId = checkedId;
            RadioButton selectedRadioButton = findViewById(checkedId);
            // Set text color and button color when checked
            selectedRadioButton.setTextColor(getResources().getColor(R.color.radio_button_selected));
            selectedRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.radio_button_selected)));

            // Reset the other radio buttons to default color
            for (int i = 0; i < radioGroupShift.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroupShift.getChildAt(i);
                if (radioButton.getId() != checkedId) {
                    radioButton.setTextColor(getResources().getColor(R.color.radio_button_default));
                    radioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.radio_button_default)));
                }
            }
        });

    }

    private void checkInQrCode(String qrCodeString) {
        Log.d("CheckInDailyActivity", "QR CODE ĐỂ GỌI HÀM: "+ qrCodeString);
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        int isConfirmed = 0; // Đã xác nhận
        String qrContent = qrCodeString; // QR content ví dụ

        // Tìm Shift dựa trên selectedShiftId
        ShiftModel.Shift selectedShift = null;
        for (ShiftModel.Shift shift : workShiftList) {
            if (shift.getWorkShiftId() == selectedShiftId) {
                selectedShift = shift;
                break;
            }
        }

        if (selectedShift != null) {
            int shiftType = selectedShift.getShiftType(); // Loại ca
            int workShiftId = selectedShift.getWorkShiftId(); // ID ca làm việc từ radio button đã chọn

            // Tạo đối tượng CheckInQrCodeRequest
            CheckInQrCodeRequest checkInQrCodeRequest = new CheckInQrCodeRequest(deviceId, isConfirmed, qrContent, shiftType, workShiftId);

            // Lấy token từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            if (token != null) {
                // Gọi API check-in
                Call<ApiResponse<String>> call = apiInterface.checkInQrCode(token, checkInQrCodeRequest);
                call.enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<String> apiResponse = response.body();
                            if (apiResponse.getStatus() == 200) {
                                // Đăng ký thành công
                                Toast.makeText(CheckInDailyActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                            } else {
                                // Xử lý nếu có lỗi từ API
                                Toast.makeText(CheckInDailyActivity.this, "Lỗi check-in: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CheckInDailyActivity.this, "Lỗi kết nối: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        Toast.makeText(CheckInDailyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CheckInDailyActivity.this, "Token không tồn tại", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CheckInDailyActivity.this, "Ca làm việc không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
        barcodeScannerView.decodeSingle(result -> {
            if (selectedShiftId == -1) {
                Toast.makeText(CheckInDailyActivity.this, "Vui lòng chọn ca trước khi quét QR", Toast.LENGTH_SHORT).show();
            } else if (result != null) {
                String qrCodeString = result.getText();
                Log.d("CheckInDailyActivity", "QR Code: " + qrCodeString);
                checkInQrCode(qrCodeString); // Gọi API check-in
            } else {
                Toast.makeText(CheckInDailyActivity.this, "Quét QR thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeScannerView.pause();
    }
}
