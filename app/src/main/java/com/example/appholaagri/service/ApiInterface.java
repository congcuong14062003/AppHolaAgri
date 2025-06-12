package com.example.appholaagri.service;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ChangePassModel.ChangePassRequest;
import com.example.appholaagri.model.CheckInInitFormData.CheckInInitFormData;
import com.example.appholaagri.model.CheckInQrCodeModel.CheckInQrCodeRequest;
import com.example.appholaagri.model.CheckPhoneModel.CheckPhoneRequest;
import com.example.appholaagri.model.ColAndRowNumberModel.ColAndRowNumberResponse;
import com.example.appholaagri.model.DepartmentModel.Department;
import com.example.appholaagri.model.DirectMeasurement.DirectMeasurementRequest;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilRequest;
import com.example.appholaagri.model.FluctuationSoilModel.FluctuationSoilResponse;
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantRequest;
import com.example.appholaagri.model.IdentificationPlantModel.IdentificationPlantResponse;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorRequest;
import com.example.appholaagri.model.IdentificationSensorModel.IdentificationSensorResponse;
import com.example.appholaagri.model.ListPlantModel.ListPlantResponse;
import com.example.appholaagri.model.ListSensorModel.ListSensorRequest;
import com.example.appholaagri.model.ListSensorModel.ListSensorResponse;
import com.example.appholaagri.model.ListUserModel.ListUserRequest;
import com.example.appholaagri.model.ListUserModel.ListUserResponse;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftRequest;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse;
import com.example.appholaagri.model.ListWorkShiftModel.WorkShiftListWrapper;
import com.example.appholaagri.model.LoginModel.LoginData;
import com.example.appholaagri.model.LoginModel.LoginRequest;
import com.example.appholaagri.model.MenuHomeModel.MenuHomeResponse;
import com.example.appholaagri.model.PlanAppInitFormModel.PlanAppInitFormResponse;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.model.PlantInforByQrCode.PlantInforResponse;
import com.example.appholaagri.model.PlantationListModel.PlantationListRequest;
import com.example.appholaagri.model.PlantationListModel.PlantationListResponse;
import com.example.appholaagri.model.PlantingDateModel.PlantingDateResponse;
import com.example.appholaagri.model.RecordConditionModel.InforRecordConditionByQrCode;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionRequest;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionTabList;
import com.example.appholaagri.model.RequestDetailModel.Comments;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupRequest;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.RequestModel.RequestListRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailData;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailRequest;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableData;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableRequest;
import com.example.appholaagri.model.SensorAppInitFormModel.SensorAppInitFormResponse;
import com.example.appholaagri.model.ShiftDetailModel.ShiftDetailRequest;
import com.example.appholaagri.model.ShiftDetailModel.ShiftDetailResponse;
import com.example.appholaagri.model.ShiftModel.ShiftModel;
import com.example.appholaagri.model.SoilDetailModel.SoilDetailRespose;
import com.example.appholaagri.model.SoilManualInitFormModel.SoilManualInitFormResponse;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageRequest;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.model.UploadFileModel.UploadFileResponse;
import com.example.appholaagri.model.UserData.UserData;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Part;
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

    // danh sách tổng hợp công
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
    Call<ApiResponse<String>> dayOffCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // Đăng ký mua sắm vật tư
    @POST("request/buy-new")
    Call<ApiResponse<String>> buyNewCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // Đơn xin làm thêm
    @POST("request/over-time")
    Call<ApiResponse<String>> overTimeCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);    // Đơn xin làm thêm

    // đơn xin đi muộn về sớm
    @POST("request/late-early")
    Call<ApiResponse<String>> lateEarlyCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // đơn xin thôi việc

    @POST("request/resign")
    Call<ApiResponse<String>> resignCreateRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);

    // chỉnh sửa đề xuất
    @POST("request/modify")
    Call<ApiResponse<String>> modifyRequest(@Header("Authorization") String token, @Body GroupRequestCreateRequest groupRequestCreateRequest);


    // request new

    @POST("request/resign")
    Call<ApiResponse<String>> createBaseRequest(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);

    // đơn xin đi muộn về sớm
    @POST("request/late-early")
    Call<ApiResponse<String>> lateEarlyCreateRequestNew(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);
    // Đơn xin nghỉ phép
    @POST("request/day-off")
    Call<ApiResponse<String>> dayOffCreateRequestNew(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);

    // Đăng ký mua sắm vật tư
    @POST("request/buy-new")
    Call<ApiResponse<String>> buyNewCreateRequestNew(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);


    // Đơn xin làm thêm
    @POST("request/over-time")
    Call<ApiResponse<String>> overTimeCreateRequestNew(@Header("Authorization") String token, @Body  RequestDetailData requestDetailData);

    // đơn xin thôi việc

    @POST("request/resign")
    Call<ApiResponse<String>> resignCreateRequestNew(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);


    // Tờ trình công tác
    @POST("request/business-trip")
    Call<ApiResponse<String>> businessTripCreateRequestNew(@Header("Authorization") String token, @Body  RequestDetailData requestDetailData);
    // chỉnh sửa đề xuất
    @POST("request/modify")
    Call<ApiResponse<String>> modifyRequestBase(@Header("Authorization") String token, @Body RequestDetailData requestDetailData);


    // định danh
    // init form
    @GET("plant-app/init-form")
    Call<ApiResponse<PlanAppInitFormResponse>> planInitForm(@Header("Authorization") String token);

    // init form date
    @GET("plant-app/init-form-date")
    Call<ApiResponse<PlantingDateResponse>> planInitFormDate(
            @Header("Authorization") String token,
            @Query("idCropVarieties") int idCropVarieties,
            @Query("idCultivationArea") int idCultivationArea,
            @Query("idPlantation") int idPlantation);

    // row form plant
    @GET("plant-app/row-form")
    Call<ApiResponse<List<ColAndRowNumberResponse>>> planRowFormFormDate(
            @Header("Authorization") String token,
            @Query("idCultivationArea") int idCultivationArea,
            @Query("qrCode") String qrCode);
    // column form plant
    @GET("plant-app/column-form")
    Call<ApiResponse<List<ColAndRowNumberResponse>>> planColumnFormFormDate(
            @Header("Authorization") String token,
            @Query("idCultivationArea") int idCultivationArea,
            @Query("qrCode") String qrCode,
            @Query("rowIn") int rowIn);
    // định danh cây trồng
    @POST("plant-app/identification-plant")
    Call<ApiResponse<IdentificationPlantResponse>> identificationPlant(@Header("Authorization") String token, @Body IdentificationPlantRequest identificationPlantRequest);

    // init form
    @GET("sensor-app/init-form")
    Call<ApiResponse<SensorAppInitFormResponse>> sensorInitForm(@Header("Authorization") String token, @Query("qrCode") String qrCode);

    // định danh cảm biến
    @POST("sensor-app/identification-sensor")
    Call<ApiResponse<IdentificationSensorResponse>> identificationSensor(@Header("Authorization") String token, @Body IdentificationSensorRequest identificationSensorRequest);    // định danh cảm biến

    // danh sách đồn điền
    @POST("plantation/list")
    Call<ApiResponse<PlantationListResponse>> listPlantation(@Header("Authorization") String token, @Body PlantationListRequest plantationListRequest);

    // danh sách cảm biến
    @POST("monitoring-app/list")
    Call<ApiResponse<ListSensorResponse>> listSensor(@Header("Authorization") String token, @Body ListSensorRequest listSensorRequest);


    // danh sách cây trồng
    @POST("plant-app/list-plant")
    Call<ApiResponse<ListPlantResponse>> listPlant(@Header("Authorization") String token, @Body ListSensorRequest listSensorRequest);


    // danh sách cây trồng
    @GET("plant-app/detail-plant")
    Call<ApiResponse<PlantDetailResponse>> detailPlant(@Header("Authorization") String token, @Query("id") int id);

    // ghi nhận tình trạng
    @POST("plant-app/list-examination")
    Call<ApiResponse<RecordConditionResponse>> recordCondition(@Header("Authorization") String token, @Body RecordConditionRequest recordConditionRequest);


    // danh sách tab ghi nhận tình trạng cây trồng
    @GET("plant-app/status-examination")
    Call<ApiResponse<List<RecordConditionTabList>>> recordConditionTabList(@Header("Authorization") String token);


    // API quét Qr ghi nhận tình trạng cây
    @GET("plant-app/qr-examination")
    Call<ApiResponse<InforRecordConditionByQrCode>> qrContentRecordCondition(@Header("Authorization") String token, @Query("qrCode") String qrCode);

    // ghi nhận/ xác nhận tình trạng cây
    @POST("plant-app/examination")
    Call<ApiResponse<String>> confirmRecordCondition(@Header("Authorization") String token, @Body InforRecordConditionByQrCode inforRecordConditionByQrCode);

    // API chi tiết tình trạng cây
    @GET("plant-app/detail-examination")
    Call<ApiResponse<InforRecordConditionByQrCode>> qrContentDetailRecordCondition(@Header("Authorization") String token, @Query("id") Integer id);
    // API upload file
    @Multipart
    @POST("utility/upload-file")
    Call<ApiResponse<UploadFileResponse>> uploadFile(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );



    @GET("soil-information/detail-soil")
    Call<ApiResponse<SoilDetailRespose>> detailSoil(
            @Header("Authorization") String token,
            @Query("idPlant") Integer idPlant,
            @Query("idMonitoring") Integer idMonitoring
    );


    //
    @POST("soil-information/fluctuation-soil")
    Call<ApiResponse<FluctuationSoilResponse>> fluctuationSoil(@Header("Authorization") String token, @Body FluctuationSoilRequest fluctuationSoilRequest);

    // danh sách các tab phân ca
    @POST("work-shift/get-tab")
    Call<ApiResponse<List<RecordConditionTabList>>> workShiftTab(@Header("Authorization") String token);

    // danh sách các tab phân ca
    @POST("work-shift/get-list")
    Call<ApiResponse<WorkShiftListWrapper>> listWokShift(@Header("Authorization") String token, @Body ListWorkShiftRequest listWorkShiftRequest);

    // danh sách các tab phân ca
    @POST("work-shift/get-detail")
    Call<ApiResponse<WorkShiftListWrapper>> detailWokShift(@Header("Authorization") String token, @Body ShiftDetailRequest shiftDetailRequest);


    // soil-manual


    // api đo thông tin cầm tay
    @POST("soil-manual/direct-measurement")
    Call<ApiResponse<String>> directMeasurement(@Header("Authorization") String token, @Body DirectMeasurementRequest directMeasurementRequest);

    // API thông tin chi tiết cây thu thập thủ công
    @GET("soil-manual/init-form")
    Call<ApiResponse<SoilManualInitFormResponse>> initFormPlantByQrCode(@Header("Authorization") String token, @Query("qrCode") String qrCode);

    // api chiến dịch

    // api đo thông tin cầm tay
    @POST("soil-manual/direct-measurement")
    Call<ApiResponse<String>> listCampaign(@Header("Authorization") String token, @Body DirectMeasurementRequest directMeasurementRequest);





    @GET("app-view/list-block")
    Call<ApiResponse<List<MenuHomeResponse.MenuItem>>> menuHome(
            @Header("Authorization") String token,
            @Query("screenCode") String screenCode
    );


    // list user
    @POST("user/list-user")
    Call<ApiResponse<List<Follower>>> listUser(@Header("Authorization") String token, @Body ListUserRequest listUserRequest);

    // list department
    @GET("user/list-department")
    Call<ApiResponse<List<Department>>> listDepartment(@Header("Authorization") String token);

    // create comments
    @POST("request-comment/create-comment")
    Call<ApiResponse<String>> createComment(@Header("Authorization") String token, @Body Comments comments);

}
