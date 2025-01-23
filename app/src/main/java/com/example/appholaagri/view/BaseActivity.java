package com.example.appholaagri.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


import com.example.appholaagri.service.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseActivity extends AppCompatActivity {
    private boolean isActivityVisible = false;

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActivityVisible && !NetworkUtil.isNetworkAvailable(context)) {
                showNetworkSnackbar();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }

    private void showNetworkSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Mất kết nối mạng", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", view -> {
            snackbar.dismiss();
        });
        snackbar.show();

        new Handler().postDelayed(() -> {
            snackbar.dismiss();
            if (isActivityVisible) {
                Intent noNetworkIntent = new Intent(this, ErrorConnectionActivity.class);
                startActivity(noNetworkIntent);

            }
        }, 400);
    }
}
