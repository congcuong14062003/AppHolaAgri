package com.example.appholaagri.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Xử lý khi nhận broadcast ở đây
        Log.d("MyReceiver", "Broadcast received!");
    }
}
