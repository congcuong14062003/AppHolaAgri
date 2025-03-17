package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.SalaryTableAdapterTabList;
import com.example.appholaagri.adapter.TimekeepingStatisticsAdapterTabList;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SalaryTableActivity extends AppCompatActivity {
    private TabLayout tabLayoutSalary;
    private ViewPager2 viewPagerSalary;
    private SalaryTableAdapterTabList salaryTableAdapterTabList;
    private ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salary_table);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        tabLayoutSalary = findViewById(R.id.tabLayoutSalary);
        viewPagerSalary = findViewById(R.id.viewPagerSalary);
        backBtnReview = findViewById(R.id.backBtnReview);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        // Thiết lập Adapter cho ViewPager
        salaryTableAdapterTabList = new SalaryTableAdapterTabList(this);
        viewPagerSalary.setAdapter(salaryTableAdapterTabList);
        new TabLayoutMediator(tabLayoutSalary, viewPagerSalary, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Bảng công");
                    break;
                case 1:
                    tab.setText("Bảng lương");
                    break;
            }
        }).attach();

    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(SalaryTableActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home");
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
}