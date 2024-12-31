package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassActivity extends AppCompatActivity {
    private EditText newPassInput, confirmPassInput;
    Button change_pass_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        Log.d("NewPassActivity", "deviceId: " + deviceId);
        Log.d("NewPassActivity", "phoneNumber: " + phoneNumber);
        // Liên kết View
        newPassInput = findViewById(R.id.new_pass_input);
        confirmPassInput = findViewById(R.id.confirm_pass_input);
        change_pass_button = findViewById(R.id.change_pass_button);
        change_pass_button.setOnClickListener(view -> {
            String newPassword = newPassInput.getText().toString().trim();
            String confirmPassword = confirmPassInput.getText().toString().trim();

            if (validatePasswords(newPassword, confirmPassword)) {
                String hashedPassword = Utils.hashPassword(newPassword);
                // Gọi API đổi mật khẩu
                changePassword(deviceId, phoneNumber, hashedPassword);
            }
        });
    }
    private boolean validatePasswords(String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void changePassword(String deviceId, String phoneNumber, String hashedPassword) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        int isMobile = 1;  // Giả sử là điện thoại
        int rememberMe = 1;  // Không nhớ mật khẩu
        String requestId = "requestId123";  // Thay bằng giá trị thực tế hoặc tạo UUID
        int serialVersionUID = 1;  // Giả sử giá trị mặc định

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(deviceId, isMobile, hashedPassword, rememberMe, requestId, serialVersionUID, phoneNumber);

        Call<ApiResponse<String>> call = apiInterface.forgotPassword(forgotPasswordRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                Log.d("NewPassActivity", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        Toast.makeText(NewPassActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewPassActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(NewPassActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewPassActivity.this, "Lỗi kết nối, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(NewPassActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}