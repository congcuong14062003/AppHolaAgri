package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.appholaagri.R;
import com.example.appholaagri.databinding.ActivityPlantationDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlantationDetailActivity extends AppCompatActivity {

    private ActivityPlantationDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Ánh xạ layout bằng View Binding
        binding = ActivityPlantationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ẩn thanh điều hướng mặc định
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        // Đặt Fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(new PlatationDetailMapFragment());
        }
        // Kiểm tra nếu cần điều hướng đến SettingFragment
        String navigateTo = getIntent().getStringExtra("navigate_to");
        if ("plant".equals(navigateTo)) {
            replaceFragment(new PlantFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_plant);  // Đánh dấu mục Setting là active
        } else if ("sensor".equals(navigateTo)) {
            replaceFragment(new SensorFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_sensor);  // Đánh dấu mục Setting là active
        }
        // Nhận plantationId từ Intent
        int plantationId = getIntent().getIntExtra("plantationId", -1);



        // Xử lý Bottom Navigation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_map:
                    selectedFragment = new PlatationDetailMapFragment();
                    break;
                case R.id.nav_plant:
                    selectedFragment = new PlantFragment();
                    break;
                case R.id.nav_sensor:
                    selectedFragment = new SensorFragment();
                    break;
            }

            if (selectedFragment != null) {
                // Truyền plantationId vào Fragment mới
                Bundle bundle = new Bundle();
                bundle.putInt("plantationId", plantationId);
                selectedFragment.setArguments(bundle);
                replaceFragment(selectedFragment);
            }
            return true;
        });

        // Xử lý padding để tránh bị che bởi System Bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Hàm thay đổi Fragment
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
