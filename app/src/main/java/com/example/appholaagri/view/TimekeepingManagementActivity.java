package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimekeepingManageAdapter;
import com.example.appholaagri.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class TimekeepingManagementActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TimekeepingManageAdapter timekeepingManageAdapter;
    private Calendar selectedDate = Calendar.getInstance(); // Lưu giữ ngày tháng hiện tại
    private TextView tab2Title;
    private ImageView calendarIcon, backBtnReview;
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
        timekeepingManageAdapter = new TimekeepingManageAdapter(this);
        viewPager.setAdapter(timekeepingManageAdapter);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });

        // Liên kết TabLayout với ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chờ xác nhận");
                    break;
                case 1:
                    tab.setText("Xác nhận");
                    break;
                case 2:
                    tab.setText("Từ chô");
                    break;
            }
        }).attach();
    }
}