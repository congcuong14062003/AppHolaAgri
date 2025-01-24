package com.example.appholaagri.service;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.model.CheckInInitFormData.CheckInInitFormData;
import com.example.appholaagri.model.CheckInQrCodeModel.CheckInQrCodeRequest;
import com.example.appholaagri.model.CheckPhoneModel.CheckPhoneRequest;
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
import com.example.appholaagri.model.LoginModel.LoginData;
import com.example.appholaagri.model.LoginModel.LoginRequest;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateResponse;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupRequest;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.RequestModel.RequestListRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusData;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailData;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailRequest;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableData;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableRequest;
import com.example.appholaagri.model.ShiftModel.ShiftModel;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageRequest;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.model.UserData.UserData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface ApiInterface {
    // api đăng nhập
    @POST("public/login")
    // Replace with your login endpoint
    Call<ApiResponse<LoginData>> login(@Body LoginRequest loginRequest);

    // api check số điện thoại
    @POST("auth/check-permission")
    // Replace with your actual endpoint
    Call<ApiResponse<String>> checkPhone(@Body CheckPhoneRequest checkPhoneRequest);

    // api quên mật khẩu
    @POST("user/forget-password")
    // Replace with your actual endpoint
    Call<ApiResponse<String>> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    // api đổi mật khẩu
    @POST("user/change-password")
    // Replace with your login endpoint
    Call<ApiResponse<String>> changePassword(@Header("Authorization") String token, @Body ChangePassRequest changePassRequest);

    // api đăng xuất
    @POST("auth/logout")
    // Thay endpoint thành "user/detail" (hoặc endpoint thực tế của bạn)
    Call<ApiResponse<String>> handleLogout(@Header("Authorization") String token);

    // api xem thông tin cá nhân
    @GET("user/detail")
    // Thay endpoint thành "user/detail" (hoặc endpoint thực tế của bạn)
    Call<ApiResponse<UserData>> userData(@Header("Authorization") String token);

    // api danh sách ca làm việc
    @GET("check-in/list-shift")
    Call<ApiResponse<List<ShiftModel.Shift>>> shiftModel(@Header("Authorization") String token);

    // check in qr code
    @POST("check-in/daily")
    Call<ApiResponse<String>> checkInQrCode(@Header("Authorization") String token, @Body CheckInQrCodeRequest checkInQrCodeRequest);

    // thống kê chấm công
    @POST("check-in/checkin-logs")
    Call<ApiResponse<TimekeepingStatisticsData>> timekeepingStatistics(@Header("Authorization") String token, @Body TimekeepingStatisticsRequest timekeepingStatisticsRequest);

    // danh sách tab quản lý chấm công
    @GET("check-in/init-form")
    Call<ApiResponse<CheckInInitFormData>> checkInInitFormData(@Header("Authorization") String token);

    // danh sách quản lý chấm công (chờ xác nhận, từ chối, xác nhận)
    @POST("check-in/list-attend")
    Call<ApiResponse<List<TimeKeepingManageData>>> timeKeeingManageData(@Header("Authorization") String token, @Body TimeKeepingManageRequest timeKeepingManageRequest);

    // danh sách tổng hợp côn
    @POST("work-summary/get-list")
    Call<ApiResponse<SalaryTableData>> salaryTableData(@Header("Authorization") String token, @Body SalaryTableRequest salaryTableRequest);

    // danh sách tổng hợp côn
    @POST("work-summary/get-detail")
    Call<ApiResponse<SalaryTableDetailData>> salaryTableDetailData(@Header("Authorization") String token, @Body SalaryTableDetailRequest salaryTableDetailRequest);

    // danh sách tab yêu cầu đề - xuất
    @POST("request/request-type")
    Call<ApiResponse<RequestTabListDataResponse>> requestTabListData(@Body RequestTabListRequest requestTabListRequest); // Sử dụng @Query để truyền page và size

    // danh sách đề xuất
    @POST("request/get-list")
    Call<ApiResponse<RequestListData>> requestListData(@Header("Authorization") String token, @Body RequestListRequest requestListRequest);

    // danh sách status-request
    @POST("request/get-status")
    Call<ApiResponse<RequestStatusResponse>> requestStatusData(@Header("Authorization") String token, @Body RequestStatusRequest requestStatusRequest);

    // danh sách nhóm đề xuất
    @POST("request/request-group")
    Call<ApiResponse<RequestGroupResponse>> listRequestGroup(@Header("Authorization") String token, @Body RequestGroupRequest requestGroupRequest);

    // chi tiết để xuất
    @GET("request/detail")
    Call<ApiResponse<RequestDetailData>> requestDetailData(
            @Header("Authorization") String token,
            @Query("requestId") int requestId   // Sử dụng @Query thay vì @Header
    );

    // init form đề xuất
    @GET("request/init-form")
    Call<ApiResponse<RequestDetailData>> initCreateRequest(
            @Header("Authorization") String token,
            @Query("id") int GroupRequestId
    );

    // Đơn xin nghỉ phép
    @POST("request/day-off")
    Call<ApiResponse<GroupRequestCreateResponse>> dayOffCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // Đăng ký mua sắm vật tư
    @POST("request/buy-new")
    Call<ApiResponse<GroupRequestCreateResponse>> buyNewCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // Đơn xin nghỉ phép
    @POST("request/over-time")
    Call<ApiResponse<GroupRequestCreateResponse>> overTimeCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);
}
