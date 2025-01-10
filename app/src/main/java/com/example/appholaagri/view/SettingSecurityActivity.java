package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.ToastUtils;
import com.example.appholaagri.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingSecurityActivity extends AppCompatActivity {
    private EditText oldPassInput, newPassInput, confirmPassInput;
    Button change_pass_button;
    TextInputLayout password_input_layout, confirm_new_pass_layout, new_pass_input_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_security);
        password_input_layout = findViewById(R.id.password_input_layout);
        new_pass_input_layout = findViewById(R.id.new_pass_input_layout);
        confirm_new_pass_layout = findViewById(R.id.confirm_new_pass_layout);
        oldPassInput = findViewById(R.id.old_password);
        newPassInput = findViewById(R.id.new_password);
        confirmPassInput = findViewById(R.id.confirm_password);
        change_pass_button = findViewById(R.id.change_pass_button);
        change_pass_button.setOnClickListener(view -> {
            String oldPassword = oldPassInput.getText().toString().trim();
            String newPassword = newPassInput.getText().toString().trim();
            String confirmPassword = confirmPassInput.getText().toString().trim();
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            if (validatePasswords(oldPassword, newPassword, confirmPassword)) {
                String hashedPassword = Utils.hashPassword(newPassword);
                String hashedOldPass = Utils.hashPassword(oldPassword);
                String hashedConfirmPass = Utils.hashPassword(confirmPassword);
                // Gọi API đổi mật khẩu
                changePassword(token, hashedConfirmPass, hashedOldPass, hashedPassword);
            }
        });
    }
    private boolean validatePasswords(String oldPassword, String newPassword, String confirmPassword) {
        boolean isValid = true;

        if (TextUtils.isEmpty(oldPassword)) {
            password_input_layout.setError("Mật khẩu hiện tại không được để trống");
            isValid = false;
        } else {
            password_input_layout.setError(null);
        }

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

    private void changePassword(String token, String confirmPass, String oldPass, String newPass) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        ChangePassRequest changePassRequest = new ChangePassRequest(confirmPass, oldPass, newPass);
        Call<ApiResponse<String>> call = apiInterface.changePassword(token, changePassRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        CustomToast.showCustomToast(SettingSecurityActivity.this,  "Đổi mật khẩu thành công");
//                        handleLogout();
                        onBackPressed();
                    } else {
                        password_input_layout.setError(apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(SettingSecurityActivity.this,  "Lỗi không xác định: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(SettingSecurityActivity.this,  "Lỗi kết nối đến máy chủ");
            }
        });
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(SettingSecurityActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "setting"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
//    private void handleLogout() {
//        try {
//            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences",MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.remove("auth_token");
//            editor.apply(); // Save changes
//            Intent intent = new Intent(SettingSecurityActivity.this, MainActivity.class); // Or LoginActivity
//            startActivity(intent);
//            finish(); // Close current activity
//        } catch (Exception e) {
//            Log.e("SettingFragment", "Error during logout: " + e.getMessage());
//        }
//    }
}