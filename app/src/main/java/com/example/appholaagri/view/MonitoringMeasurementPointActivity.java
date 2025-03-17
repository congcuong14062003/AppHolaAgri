package com.example.appholaagri.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListSoilDetailAdapter;
import com.example.appholaagri.adapter.MonitoringMeasurementPointAdapterTabList;
import com.example.appholaagri.adapter.SalaryTableAdapterTabList;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.SoilDetailModel.SoilDetailRespose;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.viewmodel.PlantViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringMeasurementPointActivity extends AppCompatActivity {
    private SoilDetailRespose soilDetailRespose;
    TextView measuring_equipment, measurement_date, measurement_point;
    ImageView backBtn;


    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MonitoringMeasurementPointAdapterTabList monitoringMeasurementPointAdapterTabList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monitoring_measurement_point);
        measuring_equipment = findViewById(R.id.measuring_equipment);
        measurement_date = findViewById(R.id.measurement_date);
        measurement_point = findViewById(R.id.measurement_point);
        backBtn = findViewById(R.id.backBtn);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        int monitoringId = getIntent().getIntExtra("monitoringId", -1);
        Log.d("MonitoringMeasurementPointActivity", "monitoringId: " + monitoringId);
        // Ánh xạ View
        monitoringMeasurementPointAdapterTabList = new MonitoringMeasurementPointAdapterTabList(this, monitoringId);
        viewPager.setAdapter(monitoringMeasurementPointAdapterTabList);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Giá trị gần nhất");
                    break;
                case 1:
                    tab.setText("Giá trị biến động");
                    break;
            }
        }).attach();
        backBtn.setOnClickListener(view -> {
            finish();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
            fetchDetailSoil(token, monitoringId);
    }
    public void fetchDetailSoil(String token, int monitoringId) {
        // Hiển thị progressBar trước khi gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<SoilDetailRespose>> call = apiInterface.detailSoil(token, null, monitoringId);

        call.enqueue(new Callback<ApiResponse<SoilDetailRespose>>() {
            @Override
            public void onResponse(Call<ApiResponse<SoilDetailRespose>> call, Response<ApiResponse<SoilDetailRespose>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SoilDetailRespose> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        soilDetailRespose = apiResponse.getData();
                        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                        String soilDetailResposes = gson.toJson(soilDetailRespose);
                        updateUi(soilDetailRespose);
                        Log.d("MonitoringMeasurementPointActivity", "soilDetailRespose: " + soilDetailResposes);
                    }
                } else {

                }
            }
            @Override
            public void onFailure(Call<ApiResponse<SoilDetailRespose>> call, Throwable t) {
            }
        });
    }
    public void updateUi(SoilDetailRespose soilDetailRespose) {
        measuring_equipment.setText(soilDetailRespose.getNameSenSor());
        measurement_date.setText(soilDetailRespose.getDate());
        measurement_point.setText(soilDetailRespose.getNameMonitoring());
    }
}