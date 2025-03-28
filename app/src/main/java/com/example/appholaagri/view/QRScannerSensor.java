package com.example.appholaagri.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScannerSensor extends BaseActivity {
    private DecoratedBarcodeView barcodeScanner;
    private ImageView closeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrscanner_sensor);
        barcodeScanner = findViewById(R.id.barcode_scanner);
        closeScanner = findViewById(R.id.close_scanner);
        TextView scan_instruction = findViewById(R.id.scan_instruction);
        String instruction = getIntent().getStringExtra("SCAN_INSTRUCTION");
        if (instruction != null) {
            scan_instruction.setText(instruction);
        }
        // Bắt đầu quét QR
        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barcodeScanner.pause();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("QR_RESULT_SENSOR", result.getText());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        // Nút đóng màn hình quét
        closeScanner.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScanner.pause();
    }
}