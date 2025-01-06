package com.example.appholaagri.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    private static Toast toast;

    /**
     * Hiển thị một Toast tùy chỉnh (dùng chung toàn app).
     *
     * @param context  Context của ứng dụng hoặc activity.
     * @param message  Thông điệp hiển thị.
     * @param duration Thời lượng hiển thị (Toast.LENGTH_SHORT hoặc Toast.LENGTH_LONG).
     */
    public static void showToast(Context context, String message, int duration) {
        if (toast != null) {
            toast.cancel(); // Hủy bỏ Toast cũ nếu đang hiển thị
        }

        toast = Toast.makeText(context.getApplicationContext(), message, duration);

        // Đặt vị trí: Trên cùng, giữa
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100); // 100 là khoảng cách từ trên xuống (pixel)
        toast.show();
    }


    /**
     * Hiển thị Toast ngắn (SHORT).
     *
     * @param context Context của ứng dụng hoặc activity.
     * @param message Thông điệp hiển thị.
     */
    public static void showShortToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * Hiển thị Toast dài (LONG).
     *
     * @param context Context của ứng dụng hoặc activity.
     * @param message Thông điệp hiển thị.
     */
    public static void showLongToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

}