package com.example.appholaagri.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListPlantCareHistoryAdapter;
import com.example.appholaagri.adapter.ListSoilDetailAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.PlantDetailModel.PlantDetailResponse;
import com.example.appholaagri.model.SoilDetailModel.SoilDetailRespose;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.viewmodel.PlantViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlantNutritionFragment extends BaseFragment {
    private PlantViewModel plantViewModel;
    private ListSoilDetailAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyStateLayout, progressBar;
    private int plantId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_plant_nutrition, container, false);
        // Ánh xạ View
        recyclerView = view.findViewById(R.id.recyclerNutrition);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        progressBar = view.findViewById(R.id.progressBar);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (getArguments() != null) {
            plantId = getArguments().getInt("plantId", -1);
            Log.d("PlantNutritionFragment", "plantId: " + plantId);
            fetchDetailSoil(token, plantId);
        }
        return view;
    }
    public void fetchDetailSoil(String token, int plantId) {
        // Hiển thị progressBar trước khi gọi API
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<ApiResponse<SoilDetailRespose>> call = apiInterface.detailSoil(token, plantId, null);

        call.enqueue(new Callback<ApiResponse<SoilDetailRespose>>() {
            @Override
            public void onResponse(Call<ApiResponse<SoilDetailRespose>> call, Response<ApiResponse<SoilDetailRespose>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SoilDetailRespose> apiResponse = response.body();

                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        List<SoilDetailRespose.SoilInfo> soilInfos = apiResponse.getData().getInfoSoil();

                        if (soilInfos != null && !soilInfos.isEmpty()) {
                            adapter = new ListSoilDetailAdapter(soilInfos);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyStateLayout.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyStateLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
                        recyclerView.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<SoilDetailRespose>> call, Throwable t) {
                // Ẩn progressBar khi có lỗi
                progressBar.setVisibility(View.GONE);

                // Hiển thị trạng thái rỗng khi gặp lỗi
                recyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            }
        });
    }

}