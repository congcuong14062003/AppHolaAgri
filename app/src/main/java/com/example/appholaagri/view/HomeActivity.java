package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.appholaagri.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FloatingActionButton checkin_daily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomAppBar, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, -bottomInset);  // Đặt padding âm để vẽ phía dưới
            return insets;
        });


        binding.bottomNavigationView.setBackground(null);
        checkin_daily = findViewById(R.id.checkin_daily);
        checkin_daily.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CheckInDailyActivity.class);
            startActivity(intent);
            finish();
        });
        // Set default fragment (HomeFragment)
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), "HOME");
        }
        // Kiểm tra nếu cần điều hướng đến SettingFragment
        String navigateTo = getIntent().getStringExtra("navigate_to");
        if ("setting".equals(navigateTo)) {
            replaceFragment(new SettingFragment(), "SETTING");
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_setting);
        } else if ("home".equals(navigateTo)) {
            replaceFragment(new HomeFragment(), "HOME");
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if ("newsletter".equals(navigateTo)) {
            replaceFragment(new RequestNewFragment(), "NEWSLETTER");
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_ban_tin);
        } else {
            // Mặc định là HomeFragment nếu không có navigate_to
            replaceFragment(new HomeFragment(), "HOME");
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment(), "HOME");
                    break;
                case R.id.nav_lich_lam:
                    replaceFragment(new WorkSchedule(), "WORK_SCHEDULE");
                    break;
                case R.id.nav_ban_tin:
                    replaceFragment(new RequestNewFragment(), "NEWSLETTER");
                    break;
                case R.id.nav_setting:
                    replaceFragment(new SettingFragment(), "SETTING");
                    break;
            }
            return true;
        });


        // Handle window insets for padding on system bars (like status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to replace fragment
    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);

        // Kiểm tra xem fragment mới có giống fragment đang hiển thị không
        if (currentFragment != null && currentFragment.getClass().equals(newFragment.getClass())) {
            return; // Nếu giống nhau, không làm gì cả
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment, tag);
        fragmentTransaction.commit();
    }
}
