package com.example.appholaagri.service;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.model.CheckInQrCodeModel.CheckInQrCodeRequest;
import com.example.appholaagri.model.CheckPhoneModel.CheckPhoneRequest;
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
import com.example.appholaagri.model.LoginModel.LoginData;
import com.example.appholaagri.model.LoginModel.LoginRequest;
import com.example.appholaagri.model.ShiftModel.ShiftModel;
import com.example.appholaagri.model.UserData.UserData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
public interface ApiInterface {
    // api đăng nhập
    @POST("public/login") // Replace with your login endpoint
    Call<ApiResponse<LoginData>> login(@Body LoginRequest loginRequest);

    // api check số điện thoại
    @POST("auth/check-permission") // Replace with your actual endpoint
    Call<ApiResponse<String>> checkPhone(@Body CheckPhoneRequest checkPhoneRequest);

    // api quên mật khẩu
    @POST("user/forget-password") // Replace with your actual endpoint
    Call<ApiResponse<String>> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    // api đổi mật khẩu
    @POST("user/change-password") // Replace with your login endpoint
    Call<ApiResponse<String>> changePassword(@Header("Authorization") String token, @Body ChangePassRequest changePassRequest);

    // api đăng xuất
    @POST("auth/logout") // Thay endpoint thành "user/detail" (hoặc endpoint thực tế của bạn)
    Call<ApiResponse<String>> handleLogout(@Header("Authorization") String token);

    // api xem thông tin cá nhân
    @GET("user/detail") // Thay endpoint thành "user/detail" (hoặc endpoint thực tế của bạn)
    Call<ApiResponse<UserData>> userData(@Header("Authorization") String token);

    // api danh sách ca làm việc
    @GET("check-in/list-shift")
    Call<ApiResponse<List<ShiftModel.Shift>>> shiftModel(@Header("Authorization") String token);

    // check in qr code
    @POST("check-in/daily")
    Call<ApiResponse<String>> checkInQrCode(@Header("Authorization") String token, @Body CheckInQrCodeRequest checkInQrCodeRequest);

}
