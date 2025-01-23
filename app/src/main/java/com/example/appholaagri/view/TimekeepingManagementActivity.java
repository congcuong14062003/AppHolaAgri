package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimekeepingManageAdapterTabList;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.CheckInInitFormData.CheckInInitFormData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimekeepingManagementActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TimekeepingManageAdapterTabList timekeepingManageAdapter;
    private Calendar selectedDate = Calendar.getInstance(); // Lưu giữ ngày tháng hiện tại
    private TextView tab2Title;
    private ImageView calendarIcon, backBtnReview;
    // Thêm biến này
    private CheckInInitFormData checkInInitFormData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timekeeping_management);

        // Kết nối các view
        tabLayout = findViewById(R.id.tabTimeManage);
        viewPager = findViewById(R.id.viewPagerManage);
        backBtnReview = findViewById(R.id.backBtnReview);
        // Thiết lập Adapter cho ViewPager
        timekeepingManageAdapter = new TimekeepingManageAdapterTabList(this);
        viewPager.setAdapter(timekeepingManageAdapter);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        getInitFormData(token);
    }

    private void getInitFormData(String token) {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<CheckInInitFormData>> call = apiInterface.checkInInitFormData(token);
        call.enqueue(new Callback<ApiResponse<CheckInInitFormData>>() {
            @Override
            public void onResponse(Call<ApiResponse<CheckInInitFormData>> call, Response<ApiResponse<CheckInInitFormData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CheckInInitFormData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        checkInInitFormData = apiResponse.getData(); // Lưu vào biến thành viên
                        updateTabs(checkInInitFormData);
                    }
                } else {
                    Log.e("TimekeepingManagementActivity", "API response is unsuccessful");
                    CustomToast.showCustomToast(TimekeepingManagementActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CheckInInitFormData>> call, Throwable t) {
                Log.e("TimekeepingManagementActivity", "Error: " + t.getMessage());
                CustomToast.showCustomToast(TimekeepingManagementActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }


    private void updateTabs(CheckInInitFormData checkInInitFormData) {
        List<CheckInInitFormData.Tab> tabs = checkInInitFormData.getListTabs();
        if (tabs != null && !tabs.isEmpty()) {
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                if (position < tabs.size()) {
                    tab.setText(tabs.get(position).getCode());
                } else {
                    tab.setText("Tab " + position);
                }
            }).attach();
            // Cập nhật ViewPager Adapter nếu cần thiết
            timekeepingManageAdapter.notifyDataSetChanged();
        }
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(TimekeepingManagementActivity.this, TimekeepingStatisticsActivity.class);
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
}