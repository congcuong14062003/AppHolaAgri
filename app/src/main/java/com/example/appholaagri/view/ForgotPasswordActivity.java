package com.example.appholaagri;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnBackLogin, nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBackLogin = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.next_forgot_btn);
        btnBackLogin.setOnClickListener(view -> {
            onBackPressed();
        });
        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, NewPassActivity.class);
            startActivity(intent);
        });
    }
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }
}