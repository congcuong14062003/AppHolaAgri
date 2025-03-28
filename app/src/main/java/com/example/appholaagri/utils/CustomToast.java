package com.example.appholaagri.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.appholaagri.R;

public class CustomToast {

    // Giá trị mặc định
    private static final int DEFAULT_TEXT_COLOR = android.graphics.Color.WHITE; // Màu chữ mặc định
    private static final int DEFAULT_PADDING_DP = 20; // Padding mặc định (đơn vị dp)

    /**
     * Hiển thị một Toast với giá trị mặc định.
     *
     * @param context Context của ứng dụng.
     * @param message Nội dung thông báo.
     */
    public static void showCustomToast(Context context, String message) {
        showCustomToast(context, message, DEFAULT_TEXT_COLOR, DEFAULT_PADDING_DP);
    }

    /**
     * Hiển thị một Toast tùy chỉnh.
     *
     * @param context   Context của ứng dụng.
     * @param message   Nội dung thông báo.
     * @param textColor Màu chữ của Toast.
     * @param paddingDp Padding (đơn vị dp).
     */
    public static void showCustomToast(Context context, String message, int textColor, int paddingDp) {
        // Chuyển đổi từ dp sang px
        int paddingPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                paddingDp,
                context.getResources().getDisplayMetrics()
        );

        // Tạo layout cho Toast
        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.VERTICAL);
        toastLayout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        toastLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_custom));

        // Tạo TextView hiển thị thông báo
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextColor(textColor);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Thêm TextView vào layout
        toastLayout.addView(textView);

        // Tạo Toast
        Toast toast = new Toast(context);
        toast.setView(toastLayout);

        // Đặt vị trí góc trên bên phải
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);  // Thêm offset 100px để nó không dính sát mép trên

        // Đặt thời gian hiển thị
        toast.setDuration(Toast.LENGTH_LONG);

        // Hiển thị Toast
        toast.show();
    }
}