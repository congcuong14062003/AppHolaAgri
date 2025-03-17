package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.PlantPagerAdapter;
import com.example.appholaagri.viewmodel.PlantViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ManagerDetailPlantActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PlantPagerAdapter adapter;
    private PlantViewModel plantViewModel;
    private ImageView backBtnReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_detail_plant);

        tabLayout = findViewById(R.id.tabRequest);
        viewPager = findViewById(R.id.viewPagerRequest);
        backBtnReview = findViewById(R.id.backBtnReview);

        backBtnReview.setOnClickListener(view -> {
            finish(); // Đóng Activity và quay lại màn hình trước đó
        });


        int plantId = getIntent().getIntExtra("plantId", -1);
        Log.d("ManagerDetailPlantActivity", "plantId: " + plantId);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Khởi tạo ViewModel
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        // Gọi API để lấy dữ liệu
        plantViewModel.fetchPlantData(token, plantId);

        // Truyền planId vào Adapter
        adapter = new PlantPagerAdapter(this, plantId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Thông tin"); break;
                case 1: tab.setText("Lịch sử chăm sóc"); break;
                case 2: tab.setText("Ghi nhận tình trạng"); break;
                case 3: tab.setText("Điều kiện dinh dưỡng"); break;
                case 4: tab.setText("Giá trị dinh dưỡng"); break;
            }
        }).attach();
    }
}
