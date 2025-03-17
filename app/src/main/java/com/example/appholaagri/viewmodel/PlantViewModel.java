package com.example.appholaagri.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantViewModel extends AndroidViewModel {
    private MutableLiveData<PlantDetailResponse> plantData = new MutableLiveData<>();

    public PlantViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PlantDetailResponse> getPlantData() {
        return plantData;
    }

    public void fetchPlantData(String token, int plantId) {
        ApiInterface apiInterface = ApiClient.getClient(getApplication()).create(ApiInterface.class);
        Call<ApiResponse<PlantDetailResponse>> call = apiInterface.detailPlant(token, plantId);

        call.enqueue(new Callback<ApiResponse<PlantDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PlantDetailResponse>> call, Response<ApiResponse<PlantDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PlantDetailResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        plantData.setValue(apiResponse.getData()); // Cập nhật dữ liệu
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PlantDetailResponse>> call, Throwable t) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}
