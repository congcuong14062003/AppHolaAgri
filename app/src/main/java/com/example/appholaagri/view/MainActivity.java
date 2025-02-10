package com.example.appholaagri.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.R;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.example.appholaagri.utils.Utils;
import com.example.appholaagri.model.LoginModel.LoginRequest;
import com.example.appholaagri.model.LoginModel.LoginData;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    Button btnForgotPass;
    Button btnLogin;
    private EditText txtPhoneNumber;
    private EditText txtPassWord;
    // pop up
    private EditText newPassInput, confirmPassInput;
    Button change_pass_button;
    TextInputLayout confirm_new_pass_layout, new_pass_input_layout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnForgotPass = findViewById(R.id.forgot_password);
        btnLogin = findViewById(R.id.login_button);
        // nút quên mật khẩu
        btnForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        txtPhoneNumber = findViewById(R.id.phone_input);
        txtPassWord = findViewById(R.id.password_input);
        btnLogin.setOnClickListener(view -> {
            // Lấy các TextInputLayout
            TextInputLayout phoneInputLayout = findViewById(R.id.phone_input_layout);
            TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);

            // Lấy giá trị từ các TextInputEditText
            String phone = txtPhoneNumber.getText().toString().trim();
            String password = txtPassWord.getText().toString().trim();

            // Xóa lỗi cũ
            phoneInputLayout.setError(null);
            passwordInputLayout.setError(null);

            boolean hasError = false;

            // Kiểm tra số điện thoại
            if (phone.isEmpty()) {
                phoneInputLayout.setError("Vui lòng nhập số điện thoại.");
                hasError = true;
            } else if (phone.length() < 10 || phone.length() > 11) {
                phoneInputLayout.setError("Số điện thoại phải từ 10 đến 11 ký tự.");
                hasError = true;
            }

            // Kiểm tra mật khẩu
            if (password.isEmpty()) {
                passwordInputLayout.setError("Vui lòng nhập mật khẩu.");
                hasError = true;
            } else if (password.length() < 6) {
                passwordInputLayout.setError("Mật khẩu phải có ít nhất 6 ký tự.");
                hasError = true;
            }

            // Nếu có lỗi, dừng xử lý
            if (hasError) return;

            // Nếu không có lỗi, xử lý đăng nhập
            String hashedPassword = Utils.hashPassword(password);
            login(phone, hashedPassword, deviceId);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra token trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token != null && !token.isEmpty()) {
            // Nếu token tồn tại, chuyển sang HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(String phone, String password, String deviceId) {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        // Cập nhật các giá trị cho LoginRequest
        int isMobile = 1;  // Giả sử là điện thoại
        int rememberMe = 1;  // Không nhớ mật khẩu
        String requestId = "requestId123";  // Thay bằng giá trị thực tế hoặc tạo UUID
        int serialVersionUID = 1;  // Giả sử giá trị mặc định
        Log.d("LoginActivity", "deviceId: " + deviceId);

        // Tạo đối tượng LoginRequest
        LoginRequest loginRequest = new LoginRequest(deviceId, isMobile, password, rememberMe, requestId, serialVersionUID, phone);

        Call<ApiResponse<LoginData>> call = apiInterface.login(loginRequest);
        call.enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {
                Log.d("LoginActivity", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        // Lấy dữ liệu đăng nhập
                        LoginData loginData = apiResponse.getData();
                        String token = loginData.getToken();
                        // Lưu token vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("old_password", password);
                        editor.putString("auth_token", token);
                        editor.apply();
                        // Kiểm tra trạng thái đăng nhập lần đầu
                        if (loginData.isFirstLogin() == true) {
                            // Hiển thị popup đổi mật khẩu, gán mật khẩu cũ
                            ChangePassRequest changePassRequest = new ChangePassRequest();
                            changePassRequest.setOldPassword(password); // Gán mật khẩu cũ
                            showChangePasswordDialog(token);
                            // Lưu mật khẩu (đã băm) vào SharedPreferences
                        } else {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        CustomToast.showCustomToast(MainActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(MainActivity.this, "Login failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable t) {
                CustomToast.showCustomToast(MainActivity.this, "Error: " + t.getMessage());
            }
        });
    }
    private void showChangePasswordDialog(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String oldPassword = sharedPreferences.getString("old_password", "");

        // Use androidx.appcompat.app.AlertDialog.Builder
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.change_password_popup, null);
        builder.setView(popupView);

        // Create the AlertDialog
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        // Initialize views
        change_pass_button = popupView.findViewById(R.id.change_pass_button);
        newPassInput = popupView.findViewById(R.id.new_pass_input);
        confirmPassInput = popupView.findViewById(R.id.confirm_pass_input);
        new_pass_input_layout = popupView.findViewById(R.id.new_pass_input_layout);
        confirm_new_pass_layout = popupView.findViewById(R.id.confirm_new_pass_layout);

        // Set up the button click listener
        change_pass_button.setOnClickListener(view -> {
            String newPassword = newPassInput.getText().toString().trim();
            String confirmPassword = confirmPassInput.getText().toString().trim();
            if (validatePasswords(newPassword, confirmPassword)) {
                String hashedPassword = Utils.hashPassword(newPassword);
                String hashedConfirmPass = Utils.hashPassword(confirmPassword);
                // Call the API to change password
                changePassword(token, hashedConfirmPass, oldPassword, hashedPassword, alertDialog);
            }
        });
        alertDialog.show();
    }




    private boolean validatePasswords(String newPassword, String confirmPassword) {
        boolean isValid = true;
        if (TextUtils.isEmpty(newPassword)) {
            new_pass_input_layout.setError("Mật khẩu mới không được để trống");
            isValid = false;
        } else if (newPassword.length() < 6) {
            new_pass_input_layout.setError("Mật khẩu phải có ít nhất 6 ký tự");
            isValid = false;
        } else {
            new_pass_input_layout.setError(null);
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirm_new_pass_layout.setError("Nhập lại mật khẩu không được để trống");
            isValid = false;
        } else if (!newPassword.equals(confirmPassword)) {
            confirm_new_pass_layout.setError("Mật khẩu xác nhận không khớp");
            isValid = false;
        } else {
            confirm_new_pass_layout.setError(null);
        }

        return isValid;
    }

    private void changePassword(String token, String confirmPass, String oldPass, String newPass, AlertDialog alertDialog) {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        ChangePassRequest changePassRequest = new ChangePassRequest(confirmPass, oldPass, newPass);
        Call<ApiResponse<String>> call = apiInterface.changePassword(token, changePassRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        CustomToast.showCustomToast(MainActivity.this, apiResponse.getMessage());
                        // Đóng popup
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        // Quay lại MainActivity
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc Activity hiện tại để không quay lại được
                    } else {
                        CustomToast.showCustomToast(MainActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(MainActivity.this, "Lỗi không xác định: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e("ChangePassword", "Error: ", t);
                CustomToast.showCustomToast(MainActivity.this, "Lỗi kết nối đến máy chủ");
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}
