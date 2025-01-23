package com.example.appholaagri.service;

import android.app.AlertDialog;
import android.app.Activity; // Thêm import này
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appholaagri.view.ErrorConnectionActivity;
import com.example.appholaagri.view.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        // Đọc nội dung phản hồi JSON mà không tiêu thụ body
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        if (responseBodyString != null) {
            try {
                // Chuyển JSON thành đối tượng để kiểm tra "status"
                JSONObject jsonResponse = new JSONObject(responseBodyString);
                int status = jsonResponse.optInt("status", 0); // Mặc định là 0 nếu không tìm thấy "status"

                if (status == 401) {
                    Log.d("AuthInterceptor", "API trả về status 401");
                    handleLogout();
                }
            } catch (Exception e) {
                Log.e("AuthInterceptor", "Lỗi khi phân tích phản hồi JSON: " + e.getMessage());
            }
        }

        return response;
    }

//    private void showLogoutPopup() {
//        Log.d("AuthInterceptor", "Vào");
//
//        // Kiểm tra xem Context có phải là Activity và Activity còn tồn tại không
//        if (context instanceof Activity && !((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
//            // Hiển thị popup trên luồng UI
//            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
//                new AlertDialog.Builder(context)
//                        .setTitle("Hệ thống đang lỗi")
//                        .setMessage("Vui lòng thử lại.")
//                        .setPositiveButton("Xác nhận", (dialog, which) -> {
//                            // Clear token và chuyển hướng đến màn hình login
//                            clearTokenAndLogout();
//                        })
//                        .setCancelable(false)
//                        .show();
//            });
//        }
//    }

    private void handleLogout() {
        try {
            // Lấy SharedPreferences và xóa token
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("auth_token");  // Kiểm tra token key ở đây
            editor.apply(); // Lưu thay đổi

            // Log để kiểm tra context và Activity
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                Log.d("AuthInterceptor", "Activity is finishing: " + activity.isFinishing());

                // Kiểm tra trạng thái của Activity
                    // Khởi tạo Intent và chuyển hướng đến MainActivity
                    Intent intent = new Intent(context, MainActivity.class); // Hoặc LoginActivity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Đảm bảo không quay lại màn hình trước đó
                    context.startActivity(intent);
                    activity.finish(); // Đóng Activity hiện tại
                    Log.d("AuthInterceptor", "Đã chuyển hướng về MainActivity và đóng Activity");
            }
        } catch (Exception e) {
            Log.e("AuthInterceptor", "Lỗi khi thực hiện logout: " + e.getMessage());
        }
    }

}
