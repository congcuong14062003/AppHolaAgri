package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnBackLogin, nextBtn;
    EditText phoneInput;
    ImageView errorIcon;
    TextView errorMessage;
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
        btnBackLogin.setOnClickListener(view -> {
            onBackPressed();
        });
        nextBtn.setOnClickListener(view -> {
            String phoneNumber = phoneInput.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            } else {
                String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                checkPhoneNumber(deviceId, phoneNumber);
            }
        });
    }


    public void checkPhoneNumber(String deviceId, String phoneNumber) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        CheckPhoneRequest checkPhoneRequest = new CheckPhoneRequest(deviceId,phoneNumber);
        Log.d("ForgotPasswordActivity", "Thông tin: " + checkPhoneRequest.getDeviceId() + checkPhoneRequest.getUserName());

        Call<ApiResponse<String>> call = apiInterface.checkPhone(checkPhoneRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
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
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối, thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }
}