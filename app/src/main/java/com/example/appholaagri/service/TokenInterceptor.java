package com.example.appholaagri.service;

import android.app.Activity;
import android.content.Context;

import okhttp3.Interceptor;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.appholaagri.view.MainActivity;

import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
public class TokenInterceptor implements Interceptor {

    private Context context;

    // Constructor nhận Context
    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Thêm token vào header nếu có
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Response response = chain.proceed(request);

        // Kiểm tra mã lỗi 401 (Unauthorized)
        if (response.code() == 401) {
            // Đăng xuất khi nhận được lỗi 401
            handleLogout();
        }

        return response;
    }

    private void handleLogout() {
        // Xóa token trong SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("auth_token").apply();

        // Điều hướng về LoginActivity hoặc MainActivity
        if (context instanceof Activity) {
            // Nếu đang ở Activity, đăng xuất và chuyển về LoginActivity
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();  // Đảm bảo đóng Activity hiện tại
        } else {
            // Nếu đang ở Fragment, lấy Activity cha và đăng xuất
            Activity activity = (Activity) context;
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();  // Đảm bảo đóng Activity cha
        }
    }
}
