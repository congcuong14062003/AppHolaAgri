package com.example.appholaagri.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.service.NetworkUtil;
import com.example.appholaagri.view.ErrorConnectionActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserDetailApiHelper {
    public interface UserDataCallback {
        void onSuccess(UserData userData);
        void onFailure(String errorMessage);
    }

    public static void getUserData(Context context, String token, UserDataCallback callback) {
        try {
            ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
            Call<ApiResponse<UserData>> call = apiInterface.userData(token);

            call.enqueue(new Callback<ApiResponse<UserData>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserData>> call, Response<ApiResponse<UserData>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<UserData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            callback.onSuccess(apiResponse.getData());
                        } else {
                            callback.onFailure("API returned status: " + apiResponse.getStatus());
                        }
                    } else {
                        callback.onFailure("API response is unsuccessful or null");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserData>> call, Throwable t) {
                    callback.onFailure("Error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("UserApiHelper", "Error calling API: " + e.getMessage());
            callback.onFailure("An error occurred while calling the API.");
        }
    }
}
