package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.R;
import com.example.appholaagri.utils.Utils;
import com.example.appholaagri.model.LoginModel.LoginRequest;
import com.example.appholaagri.model.LoginModel.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btnForgotPass;
    Button btnLogin;
    private EditText txtPhoneNumber;
    private EditText txtPassWord;
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
        Log.d("LoginActivity", "Device Id: " + deviceId);
        btnLogin.setOnClickListener(view -> {
            txtPhoneNumber = findViewById(R.id.phone_input);
            String phone = txtPhoneNumber.getText().toString();
            txtPassWord = findViewById(R.id.password_input);
            String password = txtPassWord.getText().toString();
            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(MainActivity.this, "Mật khẩu phải có ít nhất 6 số", Toast.LENGTH_SHORT).show();
                return;
            }
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
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        // Cập nhật các giá trị cho LoginRequest
        int isMobile = 1;  // Giả sử là điện thoại
        int rememberMe = 1;  // Không nhớ mật khẩu
        String requestId = "requestId123";  // Thay bằng giá trị thực tế hoặc tạo UUID
        int serialVersionUID = 1;  // Giả sử giá trị mặc định

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
                        Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        // Lấy dữ liệu đăng nhập
                        LoginData loginData = apiResponse.getData();
                        String token = loginData.getToken();
                        // Lưu token vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("auth_token", token);
                        editor.apply();
                        // Kiểm tra trạng thái đăng nhập lần đầu
                        if (loginData.isFirstLogin()) {
                            // Hiển thị popup đổi mật khẩu, gán mật khẩu cũ
                            ChangePassRequest changePassRequest = new ChangePassRequest();
                            changePassRequest.setOldPassword(password); // Gán mật khẩu cũ
                            showChangePasswordDialog(token);
                        } else {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Error message: " + apiResponse.getMessage());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                    Log.d("LoginActivity", "Error response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", "Network error: " + t.getMessage());
            }
        });
    }
    private void showChangePasswordDialog(String token) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu");
        builder.setMessage("Đây là lần đầu bạn đăng nhập, vui lòng đổi mật khẩu mới.");

        // Tạo layout chứa 3 trường nhập mật khẩu
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText oldPasswordInput = new EditText(this);
        oldPasswordInput.setHint("Nhập mật khẩu cũ");
        oldPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(oldPasswordInput);

        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("Nhập mật khẩu mới");
        newPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPasswordInput);

        final EditText confirmPasswordInput = new EditText(this);
        confirmPasswordInput.setHint("Xác nhận mật khẩu mới");
        confirmPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(confirmPasswordInput);

        builder.setView(layout);

        builder.setPositiveButton("Đổi mật khẩu", (dialog, which) -> {
            String oldPassword = oldPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (newPassword.isEmpty() || newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            } else {
                // Gửi yêu cầu đổi mật khẩu
                changePassword(token, oldPassword, newPassword, confirmPassword);
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void changePassword(String token, String oldPassword, String newPassword, String confirmPassword) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        // Tạo đối tượng ChangePasswordRequest
        ChangePassRequest request = new ChangePassRequest(oldPassword, newPassword, confirmPassword);
        Call<ApiResponse<String>> call = apiInterface.changePassword(token, request);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Đổi mật khẩu thất bại, thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
