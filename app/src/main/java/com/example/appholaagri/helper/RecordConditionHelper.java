package com.example.appholaagri.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionRequest;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.Collections;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordConditionHelper {

    public static void fetchRecordCondition(
            Context context,
            int page,
            int status,
            Consumer<RecordConditionResponse> onSuccess,
            Consumer<String> onError
    ) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object
        RecordConditionRequest request = new RecordConditionRequest(
                Collections.singletonList(-1), "", "", page,
                Collections.singletonList(-1), 20, "",
                Collections.singletonList(status), Collections.singletonList(-1)
        );

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Gọi API
        Call<ApiResponse<RecordConditionResponse>> call = apiInterface.recordCondition(token, request);
        call.enqueue(new Callback<ApiResponse<RecordConditionResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RecordConditionResponse>> call, Response<ApiResponse<RecordConditionResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RecordConditionResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        // Nếu thành công, trả về dữ liệu RecordConditionResponse
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        // Nếu API trả lỗi, gọi callback với thông báo lỗi
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        CustomToast.showCustomToast(context, errorMessage);
                        Log.e("RecordConditionHelper", errorMessage);
                    }
                } else {
                    // Nếu API không thành công
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    CustomToast.showCustomToast(context, errorMessage);
                    Log.e("RecordConditionHelper", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RecordConditionResponse>> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("RecordConditionHelper", errorMessage);
            }
        });
    }
}
