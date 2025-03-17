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
            replaceFragment(new HomeFragment());
        }
        // Kiểm tra nếu cần điều hướng đến SettingFragment
        String navigateTo = getIntent().getStringExtra("navigate_to");
        if ("setting".equals(navigateTo)) {
            replaceFragment(new SettingFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_setting);  // Đánh dấu mục Setting là active
        } else if ("home".equals(navigateTo)) {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);  // Đánh dấu mục Setting là active
        }
        // Set the listener for bottom navigation item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nav_lich_lam:
                    replaceFragment(new WorkSchedule());
                    break;
                case R.id.nav_ban_tin:
                    replaceFragment(new NewsletterFragment());
                    break;
                case R.id.nav_setting:
                    replaceFragment(new SettingFragment());
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
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
