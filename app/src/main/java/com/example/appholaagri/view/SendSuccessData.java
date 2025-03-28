package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.appholaagri.R;

public class SendSuccessData extends BaseActivity {
    private AppCompatButton back_home_btn, continue_reporting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_success_data);

        // Đúng cú pháp: Gán biến bằng findViewById từ Activity
        continue_reporting_btn = findViewById(R.id.continue_reporting_btn);
        back_home_btn = findViewById(R.id.back_home_btn);

        // Nhận tab_index từ Intent
        int tabIndex = getIntent().getIntExtra("tab_index", 0); // Mặc định là 0 nếu không có gì

        continue_reporting_btn.setOnClickListener(view -> {
            // Truyền tab_index ngược lại cho DeclarationIdentifierActivity
            Intent intent = new Intent(SendSuccessData.this, DeclarationIdentifierActivity.class);
            intent.putExtra("tab_index", tabIndex); // Sử dụng tab đã nhận được
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Đóng SendSuccessData
        });
        back_home_btn.setOnClickListener(view -> {
            onBackPressed();
        });
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(SendSuccessData.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }

}

