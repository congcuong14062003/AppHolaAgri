package com.example.appholaagri.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private String fcmToken; // Biến lưu FCM Token
    Button btnForgotPass;
    Button btnLogin;
    private EditText txtPhoneNumber;
    private EditText txtPassWord;
    // pop up
    private EditText newPassInput, confirmPassInput;
    Button change_pass_button;
    TextInputLayout confirm_new_pass_layout, new_pass_input_layout;

    private Dialog loadingDialog;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo ActivityResultLauncher để xử lý quyền thông báo
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (isGranted) {
                editor.putBoolean("notifications_enabled", true);
                CustomToast.showCustomToast(this, "Thông báo đã được bật.");
            } else {
                editor.putBoolean("notifications_enabled", false);
                CustomToast.showCustomToast(this, "Thông báo đã bị từ chối.");
            }
            editor.apply();
            // Tiếp tục xử lý sau khi yêu cầu quyền
            proceedAfterNotificationDialog(sharedPreferences.getString("auth_token", ""), getLoginData());
        });

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

        btnForgotPass = findViewById(R.id.forgot_password);
        btnLogin = findViewById(R.id.login_button);
        // nút quên mật khẩu
        btnForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

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

            // Kiểm tra FCM Token
            if (fcmToken == null || fcmToken.isEmpty()) {
                CustomToast.showCustomToast(MainActivity.this, "Không thể lấy FCM Token. Vui lòng thử lại.");
                hasError = true;
            }

            // Nếu có lỗi, dừng xử lý
            if (hasError) return;

            // Nếu không có lỗi, xử lý đăng nhập
            String hashedPassword = Utils.hashPassword(password);
            login(phone, hashedPassword, fcmToken);
        });
    }

    // Biến tạm để lưu LoginData
    private LoginData tempLoginData;

    private void setLoginData(LoginData loginData) {
        this.tempLoginData = loginData;
    }

    private LoginData getLoginData() {
        return this.tempLoginData;
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

    // Hiển thị loading
    private void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingDialog.setContentView(R.layout.dialog_loading);
            loadingDialog.setCancelable(false);

            if (loadingDialog.getWindow() != null) {
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
        loadingDialog.show();
    }

    // Ẩn loading
    private void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void login(String phone, String password, String fcmToken) {
        showLoading();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        // Cập nhật các giá trị cho LoginRequest
        int isMobile = 1;  // Giả sử là điện thoại
        int rememberMe = 1;  // Không nhớ mật khẩu
        String requestId = "requestId123";  // Thay bằng giá trị thực tế hoặc tạo UUID
        int serialVersionUID = 1;  // Giả sử giá trị mặc định
        Log.d("LoginActivity", "FCM Token: " + fcmToken);

        // Tạo đối tượng LoginRequest với fcmToken
        LoginRequest loginRequest = new LoginRequest(fcmToken, isMobile, password, rememberMe, requestId, serialVersionUID, phone);

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String loginRequests = gson.toJson(loginRequest);
        Log.d("loginRequest: ", "loginRequest: " + loginRequests);
        Call<ApiResponse<LoginData>> call = apiInterface.login(loginRequest);
        call.enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {
                hideLoading(); // Ẩn loading khi hoàn thành
                Log.d("LoginActivity", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginData> apiResponse = response.body();
                    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                    String fluctuationValue = gson.toJson(response.body());
                    Log.d("LoginActivity", "dataa: " + fluctuationValue);
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
                        // Lưu LoginData tạm thời
                        setLoginData(loginData);
                        // Kiểm tra xem popup thông báo đã được hiển thị chưa
                        boolean hasShownNotificationDialog = sharedPreferences.getBoolean("has_shown_notification_dialog", false);
                        if (!hasShownNotificationDialog) {
                            // Hiển thị popup hỏi về thông báo lần đầu
                            showNotificationPermissionDialog(token, loginData);
                            // Đánh dấu đã hiển thị popup
                            editor.putBoolean("has_shown_notification_dialog", true);
                            editor.apply();
                        } else {
                            // Nếu đã hiển thị trước đó, tiếp tục xử lý
                            proceedAfterNotificationDialog(token, loginData);
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
                hideLoading(); // Ẩn loading khi hoàn thành
                CustomToast.showCustomToast(MainActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void showNotificationPermissionDialog(String token, LoginData loginData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cho phép thông báo");
        builder.setMessage("Bạn có muốn nhận thông báo từ ứng dụng?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            // Kiểm tra và yêu cầu quyền POST_NOTIFICATIONS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                    // Yêu cầu quyền
                    requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS");
                } else {
                    // Quyền đã được cấp, lưu trạng thái
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifications_enabled", true);
                    editor.apply();
                    proceedAfterNotificationDialog(token, loginData);
                }
            } else {
                // Dưới Android 13, không cần quyền rõ ràng
                SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications_enabled", true);
                editor.apply();
                proceedAfterNotificationDialog(token, loginData);
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> {
            // Lưu trạng thái từ chối thông báo
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notifications_enabled", false);
            editor.apply();
            // Tiếp tục xử lý sau khi người dùng từ chối
            proceedAfterNotificationDialog(token, loginData);
        });
        builder.setCancelable(false); // Không cho phép đóng dialog bằng nút back
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void proceedAfterNotificationDialog(String token, LoginData loginData) {
        // Kiểm tra trạng thái đăng nhập lần đầu
        if (loginData != null && loginData.isFirstLogin()) {
            // Hiển thị popup đổi mật khẩu, gán mật khẩu cũ
            ChangePassRequest changePassRequest = new ChangePassRequest();
            changePassRequest.setOldPassword(getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("old_password", ""));
            showChangePasswordDialog(token);
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
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