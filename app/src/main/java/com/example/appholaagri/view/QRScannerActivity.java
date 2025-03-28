package com.example.appholaagri.view;
import com.example.appholaagri.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;


public class QRScannerActivity extends BaseActivity {
    private DecoratedBarcodeView barcodeScanner;
    private ImageView closeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrscanner);
        barcodeScanner = findViewById(R.id.barcode_scanner);
        closeScanner = findViewById(R.id.close_scanner);
        TextView scan_instruction = findViewById(R.id.scan_instruction);
        // Bắt đầu quét QR
        // Lấy dữ liệu từ Intent
        String instruction = getIntent().getStringExtra("SCAN_INSTRUCTION");
        if (instruction != null) {
            scan_instruction.setText(instruction);
        }
        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barcodeScanner.pause();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("QR_RESULT", result.getText());
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