package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.DeclarationIdentifierAdapterTabList;
import com.example.appholaagri.adapter.SalaryTableAdapterTabList;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DeclarationIdentifierActivity extends AppCompatActivity {
    private TabLayout tabLayoutSalary;
    private ViewPager2 viewPagerSalary;
    private DeclarationIdentifierAdapterTabList declarationIdentifierAdapterTabList;
    private ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_declaration_identifier);

        tabLayoutSalary = findViewById(R.id.tabLayoutSalary);
        viewPagerSalary = findViewById(R.id.viewPagerSalary);
        backBtnReview = findViewById(R.id.backBtnReview);

        // Thiết lập Adapter cho ViewPager
        declarationIdentifierAdapterTabList = new DeclarationIdentifierAdapterTabList(this);
        viewPagerSalary.setAdapter(declarationIdentifierAdapterTabList);

        new TabLayoutMediator(tabLayoutSalary, viewPagerSalary, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Cây trồng");
                    break;
                case 1:
                    tab.setText("Cảm biến");
                    break;
            }
        }).attach();

        // Nhận tab_index và set lại ViewPager
        int tabIndex = getIntent().getIntExtra("tab_index", 0);
        viewPagerSalary.setCurrentItem(tabIndex);

        backBtnReview.setOnClickListener(view -> finish());
    }
    public int getCurrentTabIndex() {
        return viewPagerSalary.getCurrentItem(); // Lấy tab hiện tại
    }

}