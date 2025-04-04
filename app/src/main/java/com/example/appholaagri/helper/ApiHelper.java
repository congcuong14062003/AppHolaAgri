package com.example.appholaagri.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ListPlantModel.ListPlantResponse;
import com.example.appholaagri.model.ListSensorModel.ListSensorRequest;
import com.example.appholaagri.model.ListSensorModel.ListSensorResponse;
import com.example.appholaagri.model.PlantationListModel.PlantationListRequest;
import com.example.appholaagri.model.PlantationListModel.PlantationListResponse;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionRequest;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.RequestModel.RequestListRequest;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
public class ApiHelper {
    public static void fetchRequestListData(Context context,
                                            int requestType,
                                            int currentPage,
                                            String keySearch,
                                            int status,
                                            Consumer<RequestListData> onSuccess,
                                            Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

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
                    CustomToast.showCustomToast(context, errorMessage);
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

    public static void fetchTimekeepingStatistics(Context context,
                                                  String date,
                                                  int page,
                                                  int isDaily,
                                                  Consumer<TimekeepingStatisticsData> onSuccess,
                                                  Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object
        TimekeepingStatisticsRequest request = new TimekeepingStatisticsRequest();
        request.setDate(date);
        request.setIsDaily(isDaily); // 0: theo tháng, 1: theo ngày
        request.setPage(page);
        request.setSize(20);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestDetailDataJson = gson.toJson(request);
        Log.d("aaa", "bbb: " + requestDetailDataJson);
        Call<ApiResponse<TimekeepingStatisticsData>> call = apiInterface.timekeepingStatistics(token, request);
        call.enqueue(new Callback<ApiResponse<TimekeepingStatisticsData>>() {
            @Override
            public void onResponse(Call<ApiResponse<TimekeepingStatisticsData>> call, Response<ApiResponse<TimekeepingStatisticsData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TimekeepingStatisticsData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        Log.e("ApiHelper", errorMessage);
                        CustomToast.showCustomToast(context, errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                    CustomToast.showCustomToast(context, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TimekeepingStatisticsData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    public static void fetchListPlantation(Context context,
                                                  int page,
                                                  Consumer<PlantationListResponse> onSuccess,
                                                  Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object
         PlantationListRequest request = new PlantationListRequest();
                request.setIdCompany(Arrays.asList(-1));
                request.setKeySearch("");
                request.setPage(1);
                request.setSize(20);
                request.setStatus(Arrays.asList(-1));
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<PlantationListResponse>> call = apiInterface.listPlantation(token, request);
        call.enqueue(new Callback<ApiResponse<PlantationListResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PlantationListResponse>> call, Response<ApiResponse<PlantationListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PlantationListResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        CustomToast.showCustomToast(context, errorMessage);
                        Log.e("ApiHelper", errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                    CustomToast.showCustomToast(context, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PlantationListResponse>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    public static <T> void fetchListSensor(Context context,
                                           int page,
                                           T idPlantation,  // Có thể là Integer hoặc List<Integer>
                                           String keySearch,
                                           Consumer<ListSensorResponse> onSuccess,
                                           Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object sử dụng Generic
        ListSensorRequest<T> request = new ListSensorRequest<>(idPlantation, keySearch, page, 20);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<ListSensorResponse>> call = apiInterface.listSensor(token, request);
        call.enqueue(new Callback<ApiResponse<ListSensorResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ListSensorResponse>> call, Response<ApiResponse<ListSensorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ListSensorResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        Log.e("ApiHelper", errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ListSensorResponse>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }


    public static <T> void fetchListPlant(Context context,
                                           int page,
                                           T idPlantation,  // Có thể là Integer hoặc List<Integer>
                                           String keySearch,
                                           Consumer<ListPlantResponse> onSuccess,
                                           Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object sử dụng Generic
        ListSensorRequest<T> request = new ListSensorRequest<>(idPlantation, keySearch, page, 20);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<ListPlantResponse>> call = apiInterface.listPlant(token, request);
        call.enqueue(new Callback<ApiResponse<ListPlantResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ListPlantResponse>> call, Response<ApiResponse<ListPlantResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ListPlantResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        Log.e("ApiHelper", errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ListPlantResponse>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    public static void fetchListRecordCondition(Context context,
                                                int page,
                                                Consumer<RecordConditionResponse> onSuccess,
                                                Consumer<String> onError) {
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        // Tạo request object
        RecordConditionRequest request = new RecordConditionRequest(
                Collections.singletonList(-1), "", "", 1,
                Collections.singletonList(-1), 20, "",
                Collections.singletonList(-1), Collections.singletonList(-1)
        );

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        Call<ApiResponse<RecordConditionResponse>> call = apiInterface.recordCondition(token, request);
        call.enqueue(new Callback<ApiResponse<RecordConditionResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RecordConditionResponse>> call, Response<ApiResponse<RecordConditionResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RecordConditionResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        onSuccess.accept(apiResponse.getData());
                    } else {
                        String errorMessage = apiResponse.getMessage();
                        onError.accept(errorMessage);
                        CustomToast.showCustomToast(context, errorMessage);
                        Log.e("ApiHelper", errorMessage);
                    }
                } else {
                    String errorMessage = "API response failed or is null";
                    onError.accept(errorMessage);
                    Log.e("ApiHelper", errorMessage);
                    CustomToast.showCustomToast(context, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RecordConditionResponse>> call, Throwable t) {
                String errorMessage = t.getMessage();
                onError.accept(errorMessage);
                Log.e("ApiHelper", errorMessage);
            }
        });
    }
}
