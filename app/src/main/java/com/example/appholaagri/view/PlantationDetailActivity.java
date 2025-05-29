package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        // Nhận plantationId từ Intent
        int plantationId = getIntent().getIntExtra("plantationId", -1);

        Log.d("PlantationDetailActivity: ", "plantationId: " + plantationId);
        // Đặt Fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(new PlatationDetailMapFragment(), "MAP", plantationId);
        }

        // Kiểm tra nếu cần điều hướng đến một Fragment cụ thể
        String navigateTo = getIntent().getStringExtra("navigate_to");
        if ("plant".equals(navigateTo)) {
            replaceFragment(new PlantFragment(), "PLANT", plantationId);
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_plant);
        } else if ("sensor".equals(navigateTo)) {
            replaceFragment(new SensorFragment(), "SENSOR", plantationId);
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_sensor);
        }

        // Xử lý Bottom Navigation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_map:
                    replaceFragment(new PlatationDetailMapFragment(), "MAP", plantationId);
                    break;
                case R.id.nav_plant:
                    replaceFragment(new PlantFragment(), "PLANT", plantationId);
                    break;
                case R.id.nav_sensor:
                    replaceFragment(new SensorFragment(), "SENSOR", plantationId);
                    break;
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
    private void replaceFragment(Fragment newFragment, String tag, int plantationId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayout);

        // Kiểm tra nếu fragment mới giống fragment đang hiển thị
        if (currentFragment != null && currentFragment.getClass().equals(newFragment.getClass())) {
            return; // Nếu giống nhau, không thay thế
        }

        // Truyền plantationId vào fragment
        Bundle bundle = new Bundle();
        bundle.putInt("plantationId", plantationId);
        newFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, newFragment, tag);
        fragmentTransaction.commit();
    }
}
