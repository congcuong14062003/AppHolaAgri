package com.example.appholaagri.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.CheckInQrCodeModel.CheckInQrCodeRequest;
import com.example.appholaagri.model.ShiftModel.ShiftModel;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class CheckInDailyActivity extends BaseActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private RadioGroup radioGroupShift;
    private List<ShiftModel.Shift> workShiftList = new ArrayList<>();
    private int selectedShiftId = -1;
    private ApiInterface apiInterface;
    ImageView close_scanner;
    private DecoratedBarcodeView barcodeScannerView;  // Change to DecoratedBarcodeView
    private CaptureManager captureManager;
    private int reasonType = 1;
    private String txt_reason_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_daily);

        // Kiểm tra quyền Camera
        checkCameraPermission();
        // Initialize views
        radioGroupShift = findViewById(R.id.radioGroupShift);
        barcodeScannerView = findViewById(R.id.barcode_scanner);
        close_scanner = findViewById(R.id.close_scanner);
        barcodeScannerView.setStatusText("");  // Đặt thông báo là chuỗi rỗng

        // Initialize API interface
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        close_scanner.setOnClickListener(view -> {
            onBackPressed();
        });

        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token != null) {
            // Fetch shift list from API
            getShiftList(token);
        } else {
            CustomToast.showCustomToast(this, "Token không tồn tại");
        }
        // Initialize the barcode scanner
        captureManager = new CaptureManager(this, barcodeScannerView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

        decodeAndProcessQRCode();
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
                    CustomToast.showCustomToast(CheckInDailyActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ShiftModel.Shift>>> call, Throwable t) {
                CustomToast.showCustomToast(CheckInDailyActivity.this, "Lỗi kết nối: " + t.getMessage());
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
            radioButton.setButtonTintList(ContextCompat.getColorStateList(this, R.color.radio_button_selector));
            radioButton.setTextColor(ContextCompat.getColorStateList(this, R.color.radio_button_selector));
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
            decodeAndProcessQRCode();
        });


    }

    private void checkInQrCode(String qrCodeString, String reason, Integer reasonType) {
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
            Log.d("CheckInDailyActivity: ", "deviceId" + deviceId);
            Log.d("CheckInDailyActivity: ", "qrContent" + qrContent);
            Log.d("CheckInDailyActivity: ", "shiftType" + shiftType);
            Log.d("CheckInDailyActivity: ", "workShiftId" + workShiftId);
            // Tạo đối tượng CheckInQrCodeRequest
            CheckInQrCodeRequest checkInQrCodeRequest = new CheckInQrCodeRequest(deviceId, isConfirmed, qrContent, reason, reasonType, shiftType, workShiftId);

            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String requestDetailDataJson = gson.toJson(checkInQrCodeRequest);
            Log.d("aaaaa", "bbb: " + requestDetailDataJson);

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
                                    CustomToast.showCustomToast(CheckInDailyActivity.this,  apiResponse.getMessage());
                                    onBackPressed();
                            }
                            else {
                                // Xử lý nếu có lỗi từ API
                                txt_reason_error = apiResponse.getMessage();
                                showReasonDialog(qrContent);
                                CustomToast.showCustomToast(CheckInDailyActivity.this, "Lỗi check-in: " + apiResponse.getMessage());
                            }
                        } else {
                            CustomToast.showCustomToast(CheckInDailyActivity.this, "Lỗi kết nối: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        CustomToast.showCustomToast(CheckInDailyActivity.this, "Lỗi kết nối: " + t.getMessage());
                    }
                });
            }
        } else {
            CustomToast.showCustomToast(CheckInDailyActivity.this, "Ca làm việc không hợp lệ");
        }
    }


    // nhập lí do
    private void showReasonDialog(String qrCodeString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_check_in_reason, null);
        builder.setView(dialogView);

        EditText etReason;
        ImageView rbIndividual_create, rbWork_create;
        AppCompatButton btn_cancel, btn_confirm;
        TextView title_reason;

        etReason = dialogView.findViewById(R.id.etReason);
        title_reason = dialogView.findViewById(R.id.title_reason);
        rbIndividual_create = dialogView.findViewById(R.id.rbIndividual_create);
        rbWork_create = dialogView.findViewById(R.id.rbWork_create);
        btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        title_reason.setText(txt_reason_error);

        AlertDialog dialog = builder.create();

        // Làm nền của dialog trong suốt
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

//        rbIndividual_create.setBackgroundResource(R.drawable.checked_radio);
        // Đặt sự kiện click cho "lí do cá nhân"
        rbIndividual_create.setOnClickListener(v -> {
            rbIndividual_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbWork_create.setImageResource(R.drawable.no_check_radio_create); // Đặt trạng thái "không được chọn"
            reasonType = 1;
        });
        // Đặt sự kiện click cho "lí do công việc"
        rbWork_create.setOnClickListener(v -> {
            rbWork_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbIndividual_create.setImageResource(R.drawable.no_check_radio_create); // Đặt trạng thái "không được chọn"
            reasonType = 2;
        });
        btn_confirm.setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (reason.isEmpty()) {
                CustomToast.showCustomToast(CheckInDailyActivity.this, "Vui lòng nhập lý do");
            } else {
                dialog.dismiss();
                checkInQrCode(qrCodeString, reason, reasonType);
            }
        });

        etReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        dialogView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });
        findViewById(android.R.id.content).setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        btn_cancel.setOnClickListener(view -> {
            dialog.dismiss();
            decodeAndProcessQRCode();
        });
        // Khi Dialog bị ẩn, tự động quét QR lại
        dialog.setOnDismissListener(dialogInterface -> {
            decodeAndProcessQRCode();
        });

        dialog.show();
    }


    // quét qr
    private void decodeAndProcessQRCode() {
        barcodeScannerView.decodeSingle(result -> {
            if (selectedShiftId == -1) {
                CustomToast.showCustomToast(CheckInDailyActivity.this, "Vui lòng chọn ca trước khi quét QR");
            } else if (result != null) {
                checkInQrCode(result.getText(), "", null);
//                showReasonDialog(result.getText());
            } else {
                CustomToast.showCustomToast(CheckInDailyActivity.this, "Quét QR thất bại");
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu cấp quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeScannerView.resume();
                decodeAndProcessQRCode();
            } else {
                // Không hiển thị AlertDialog, chỉ thông báo từ hệ thống
                Toast.makeText(this, "Ứng dụng cần quyền Camera để hoạt động", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(CheckInDailyActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume();
            decodeAndProcessQRCode();
        } else {
            checkCameraPermission();
        }
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
