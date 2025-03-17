package com.example.appholaagri.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.service.ResendRequestCallback;

public class SendFailedData extends BaseActivity {
    private ResendRequestCallback callback;

    public void setResendRequestCallback(ResendRequestCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_failed_data);

        AppCompatButton resendButton = findViewById(R.id.resend_btn);
        resendButton.setOnClickListener(v -> {
            setResult(RESULT_OK);  // Gửi kết quả về Fragment
            finish();  // Đóng Activity
        });

    }
}
