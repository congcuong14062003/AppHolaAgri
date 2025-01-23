package com.example.appholaagri.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appholaagri.R;
import com.example.appholaagri.service.NetworkUtil;
import com.example.appholaagri.utils.CustomToast;

import android.widget.Button;
import android.widget.Toast;



public class ErrorConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_connection); // Tạo layout cho lỗi kết nối

        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(v -> {
            if (NetworkUtil.isNetworkAvailable(this)) {
                finish();
            } else {
                CustomToast.showCustomToast(this, "Vui lòng kiểm tra kết nối mạng");
            }
        });
    }
}
