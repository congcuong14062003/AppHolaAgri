package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.CheckPhoneModel.CheckPhoneRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.example.appholaagri.utils.LoadingDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {
    Button btnBackLogin, nextBtn;
    EditText phoneInput;
    ImageView errorIcon;
    TextView errorMessage;
    TextInputLayout phone_input_layout;
    private String fcmToken; // Biến lưu FCM Token
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBackLogin = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.next_forgot_btn);
        phoneInput = findViewById(R.id.phone_input_authen);
        errorIcon  = findViewById(R.id.error_icon); // ID của ImageView chứa icon lỗi
        errorMessage  = findViewById(R.id.error_message); // ID của TextView chứa thông báo lỗi
        phone_input_layout = findViewById(R.id.phone_input_layout);
        // Lấy FCM Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        fcmToken = task.getResult();
                        Log.d("MainActivity", "FCM Token: " + fcmToken);
                    } else {
                        Log.e("MainActivity", "Failed to get FCM Token: " + task.getException());
                        fcmToken = "";
                    }
                });
        // khởi tạo loading
        loadingDialog = new LoadingDialog(this); // Khởi tạo loading dialog

        btnBackLogin.setOnClickListener(view -> {
            finish();
        });
        nextBtn.setOnClickListener(view -> {
            String phoneNumber = phoneInput.getText().toString().trim();

            // Clear previous error
            phone_input_layout.setError(null);  // Remove any existing error message

            // Validate phone number
            if (phoneNumber.isEmpty()) {
                // Set error if phone number is empty
                errorMessage.setText("Vui lòng nhập số điện thoại");
                errorIcon.setVisibility(View.VISIBLE);
            } else if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
                // Set error if phone number length is not between 10 and 11 characters
                errorMessage.setText("Số điện thoại phải từ 10 đến 11 ký tự");
                errorIcon.setVisibility(View.VISIBLE);
            } else {
                // If phone number is valid, proceed with the next steps
                String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                checkPhoneNumber(fcmToken, phoneNumber);  // Call the API to check the phone number
            }
        });


    }


    public void checkPhoneNumber(String deviceId, String phoneNumber) {
        loadingDialog.show(); // Hiển thị loading khi bắt đầu gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        CheckPhoneRequest checkPhoneRequest = new CheckPhoneRequest(deviceId,phoneNumber);
        Log.d("ForgotPasswordActivity", "Thông tin: " + checkPhoneRequest.getDeviceId() + checkPhoneRequest.getUserName());

        Call<ApiResponse<String>> call = apiInterface.checkPhone(checkPhoneRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                loadingDialog.hide(); // Ẩn loading khi có phản hồi
                Log.d("ForgotPasswordActivity", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    Log.d("ForgotPasswordActivity", "apiResponse: " + apiResponse);
                    if (apiResponse.getStatus() == 200) {
                        // Chuyển sang màn hình tiếp theo
                        Intent intent = new Intent(ForgotPasswordActivity.this, NewPassActivity.class);
                        intent.putExtra("deviceId", deviceId); // Truyền deviceId
                        intent.putExtra("phoneNumber", phoneNumber); // Truyền số điện thoại
                        startActivity(intent);
                    } else {
                        errorIcon.setVisibility(View.VISIBLE);  // Hiển thị icon lỗi
                        errorMessage.setText(apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(ForgotPasswordActivity.this,  "Lỗi kết nối, thử lại!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                loadingDialog.hide(); // Ẩn loading khi có phản hồi
                CustomToast.showCustomToast(ForgotPasswordActivity.this,  "Lỗi: " + t.getMessage());
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}