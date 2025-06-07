package com.example.appholaagri;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.appholaagri.view.CreateRequestAnotherSubmissionActivity;
import com.example.appholaagri.view.CreateRequestBuyNewActivity;
import com.example.appholaagri.view.CreateRequestContractApprovalActivity;
import com.example.appholaagri.view.CreateRequestContractApprovalSmSActivity;
import com.example.appholaagri.view.CreateRequestDayOffActivity;
import com.example.appholaagri.view.CreateRequestLateEarlyActivity;
import com.example.appholaagri.view.CreateRequestOvertTimeActivity;
import com.example.appholaagri.view.CreateRequestPersonnelReportActivity;
import com.example.appholaagri.view.CreateRequestPolicyRegulationSubmissionActivity;
import com.example.appholaagri.view.CreateRequestRecruitmentFlyerActivity;
import com.example.appholaagri.view.CreateRequestResignActivity;
import com.example.appholaagri.view.CreateRequestWorkReportActivity;
import com.example.appholaagri.view.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFCMService";
    private static final String CHANNEL_ID = "FCM_CHANNEL";
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM Token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Log toàn bộ RemoteMessage
        Log.d(TAG, "===== Start of RemoteMessage Payload =====");
        Log.d(TAG, "Full Payload: " + remoteMessage.toString());

        // Log chi tiết Notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Payload:");
            Log.d(TAG, "  Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "  Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "  ImageUrl: " + remoteMessage.getNotification().getImageUrl());
            Log.d(TAG, "  Icon: " + remoteMessage.getNotification().getIcon());
            Log.d(TAG, "  Sound: " + remoteMessage.getNotification().getSound());
            Log.d(TAG, "  Tag: " + remoteMessage.getNotification().getTag());
            Log.d(TAG, "  Color: " + remoteMessage.getNotification().getColor());
            Log.d(TAG, "  ClickAction: " + remoteMessage.getNotification().getClickAction());
        } else {
            Log.d(TAG, "No Notification Payload");
        }

        // Log chi tiết Data payload
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.d(TAG, "Data Payload:");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                Log.d(TAG, "  " + entry.getKey() + ": " + entry.getValue());
            }
        } else {
            Log.d(TAG, "No Data Payload");
        }

        // Log thông tin bổ sung
        Log.d(TAG, "Additional Info:");
        Log.d(TAG, "  From: " + remoteMessage.getFrom());
        Log.d(TAG, "  MessageId: " + remoteMessage.getMessageId());
        Log.d(TAG, "  MessageType: " + remoteMessage.getMessageType());
        Log.d(TAG, "  CollapseKey: " + remoteMessage.getCollapseKey());
        Log.d(TAG, "  TimeToLive: " + remoteMessage.getTtl());
        Log.d(TAG, "  SentTime: " + remoteMessage.getSentTime());
        Log.d(TAG, "===== End of RemoteMessage Payload =====");

        // Xử lý và gửi thông báo
        String title = "Thông báo mới";
        String body = "Bạn có thông báo mới.";
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle() != null ? remoteMessage.getNotification().getTitle() : title;
            body = remoteMessage.getNotification().getBody() != null ? remoteMessage.getNotification().getBody() : body;
        } else if (data.size() > 0) {
            title = data.get("title") != null ? data.get("title") : title;
            body = data.get("body") != null ? data.get("body") : body;
        }
        sendNotification(title, body, data);
    }

    private void sendNotification(String title, String messageBody, Map<String, String> data) {
        // Lấy typeScreen và id từ data payload
        String typeScreen = data.get("typeScreen");
        String id = data.get("id");

        // Tạo Intent dựa trên typeScreen
        Intent intent = getIntentForTypeScreen(typeScreen, id);

        // Tạo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        // Tạo Notification Channel (yêu cầu cho Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "FCM Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for FCM notifications");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo và hiển thị thông báo
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logoapp)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID + (id != null ? Integer.parseInt(id) : 0), notificationBuilder.build());
    }

    private Intent getIntentForTypeScreen(String typeScreen, String id) {
        Intent intent;
        switch (typeScreen != null ? typeScreen : "") {
            // Đi muộn - về sớm
            case "LATE_EARLY":
                intent = new Intent(this, CreateRequestLateEarlyActivity.class);
                break;
            // Nghỉ phép
            case "DAY_OFF":
                intent = new Intent(this, CreateRequestDayOffActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đăng ký làm thêm
            case "OVER_TIME":
                intent = new Intent(this, CreateRequestOvertTimeActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đăng ký mua sắm vật tư
            case "BUY_NEW":
                intent = new Intent(this, CreateRequestBuyNewActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đơn xin thôi việc
            case "RESIGN":
                intent = new Intent(this, CreateRequestResignActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề nghị tạm ứng
            case "SUBSIDIZE":
                intent = new Intent(this, HomeActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề nghị thanh toán tạm ứng
            case "SUBSIDIZE_PAY":
                intent = new Intent(this, HomeActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề nghị thanh toán
            case "REQUEST_PAYMENT":
                intent = new Intent(this, HomeActivity.class); // Thay bằng Activity tương ứng
                break;
            // Tờ trình công tác
            case "BUSINESS_TRIP":
                intent = new Intent(this, CreateRequestWorkReportActivity.class); // Thay bằng Activity tương ứng
                break;
            // Yêu cầu chuyển thiết bị
            case "SWITCH_DEVICE":
                intent = new Intent(this, HomeActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất tuyển dụng
            case "RECRUITMENT":
                intent = new Intent(this, CreateRequestRecruitmentFlyerActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất phê duyệt hợp đồng
            case "CUSTOMER_CONTRACT":
                intent = new Intent(this, CreateRequestContractApprovalActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất phê duyệt hợp đồng - SMS brand name
            case "CUSTOMER_CONTRACT_BRAND_NAME":
                intent = new Intent(this, CreateRequestContractApprovalSmSActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất tờ trình chi phí, ngân sách
            case "EXPENDITURE":
                intent = new Intent(this, CreateRequestAnotherSubmissionActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất tờ trình chính sách, chế độ, quy định, nội quy
            case "POLICY":
                intent = new Intent(this, CreateRequestPolicyRegulationSubmissionActivity.class); // Thay bằng Activity tương ứng
                break;
            // Đề xuất tờ trình nhân sự
            case "EMPLOYEE_TRANSITION":
                intent = new Intent(this, CreateRequestPersonnelReportActivity.class); // Thay bằng Activity tương ứng
                break;
            default:
                intent = new Intent(this, HomeActivity.class); // Mặc định nếu không khớp
                break;
        }

        // Thêm dữ liệu id vào Intent
        if (id != null && !id.isEmpty()) {
            try {
                intent.putExtra("requestId", Integer.parseInt(id));
            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid id format: " + id, e);
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}