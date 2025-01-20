package com.example.appholaagri.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.RequestModel.RequestListRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.function.Consumer;
public class ApiHelper {
    public static void fetchRequestListData(Context context,
                                            int requestType,
                                            int currentPage,
                                            String keySearch,
                                            int status,
                                            Consumer<RequestListData> onSuccess,
                                            Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Tạo request object
        RequestListRequest request = new RequestListRequest();
        request.setRequestType(requestType);
        request.setPage(currentPage);
        request.setSize(20); // Bạn có thể chỉnh kích thước nếu cần
        request.setKeySearch(keySearch);
        request.setStatus(status);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Gọi API
        Call<ApiResponse<RequestListData>> call = apiInterface.requestListData(token, request);
        call.enqueue(new Callback<ApiResponse<RequestListData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestListData>> call, Response<ApiResponse<RequestListData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestListData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());

                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        CustomToast.showCustomToast(context, errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestListData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }
}
