package com.example.appholaagri.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageData;
import com.example.appholaagri.model.TimeKeepingManageModel.TimeKeepingManageRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimekeepingManageApiHelper {
    public interface TimekeepingApiCallback {
        void onSuccess(List<TimeKeepingManageData> data);
        void onFailure(String errorMessage);
    }

    public static void fetchTimekeepingList(
            Context context,
            int status,
            String startDate,
            String endDate,
            String keySearch,
            List<Integer> departmentIds,
            List<Integer> teamIds,
            TimekeepingApiCallback callback
    ) {
        // Nếu startDate và endDate rỗng thì mặc định ngày hôm nay
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = sdf.format(calendar.getTime());

        if (startDate == null || startDate.isEmpty()) {
            startDate = today;
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = today;
        }

        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request
        TimeKeepingManageRequest request = new TimeKeepingManageRequest(
                0, // clientType
                departmentIds,
                endDate,
                keySearch,
                null, // managerId
                null, // staffId
                startDate,
                List.of(status),
                teamIds
        );

        // Lấy token từ SharedPreferences
        String token = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        // Gọi API
        Call<ApiResponse<List<TimeKeepingManageData>>> call = apiInterface.timeKeeingManageData(token, request);
        call.enqueue(new Callback<ApiResponse<List<TimeKeepingManageData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TimeKeepingManageData>>> call, Response<ApiResponse<List<TimeKeepingManageData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String message = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onFailure(message);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TimeKeepingManageData>>> call, Throwable t) {
                Log.e("TimekeepingApiHelper", "API error: " + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }
}
