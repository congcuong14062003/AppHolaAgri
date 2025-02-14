package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.ListStatus;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDayOffDetail extends AppCompatActivity {
    TextView etNgayBatDau, etGioBatDau, notice_lately_layout, etNgayKetThuc, etGioKetThuc, etDurationLateEarly,  txt_type_request, txt_reason_refused_request, txt_name_request, txt_name_employye_request, select_method_request, txt_part_request, txt_reason_request, txt_manager_direct_request,txt_fixed_reviewer_request,txt_follower_request;
    AppCompatButton txt_status_request;
    LinearLayout reason_refused_request_layout, actionButtonContainer;
    private RecyclerView recyclerView;
    private ActionRequestDetailAdapter adapter;
    ImageView backBtnReview, rbNhieuNgay_detail, rbMotNgay_detail, rbWork_detail, rbIndividual_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_day_off_detail);
        txt_type_request = findViewById(R.id.txt_type_request);
        txt_reason_refused_request = findViewById(R.id.txt_reason_refused_request);
        txt_name_request = findViewById(R.id.txt_name_request);
        txt_name_employye_request = findViewById(R.id.txt_name_employye_request);
        txt_part_request = findViewById(R.id.txt_part_request);
        txt_status_request = findViewById(R.id.txt_status_request_detail);
        reason_refused_request_layout = findViewById(R.id.reason_refused_request_layout);
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        etGioBatDau = findViewById(R.id.etGioBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        etGioKetThuc = findViewById(R.id.etGioKetThuc);
        txt_reason_request = findViewById(R.id.txt_reason_request);
        txt_manager_direct_request = findViewById(R.id.txt_manager_direct_request);
        txt_fixed_reviewer_request = findViewById(R.id.txt_fixed_reviewer_request);
        txt_follower_request = findViewById(R.id.txt_follower_request);
        recyclerView = findViewById(R.id.recyclerViewActionRequest);
        backBtnReview = findViewById(R.id.backBtnReview);
        rbMotNgay_detail = findViewById(R.id.rbMotNgay_detail);
        rbNhieuNgay_detail = findViewById(R.id.rbNhieuNgay_detail);
        etDurationLateEarly = findViewById(R.id.etDurationLateEarly);
        rbWork_detail = findViewById(R.id.rbWork_detail);
        rbIndividual_detail = findViewById(R.id.rbIndividual_detail);
        select_method_request = findViewById(R.id.select_method_request);
        notice_lately_layout = findViewById(R.id.notice_lately_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        Intent intent = getIntent();
        if (intent != null) {
            Integer requestId = intent.getIntExtra("requestId", -1); // Nhận requestId
            String StatusRequest = intent.getStringExtra("StatusRequest");            // Nhận type
            Integer GroupRequest = intent.getIntExtra("GroupRequest", -1);            // Nhận type
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

            Log.d("RequestLateEarlyDetail", "requestId: " + requestId);
            Log.d("RequestLateEarlyDetail", "StatusRequest: " + StatusRequest);
            Log.d("RequestLateEarlyDetail", "GroupRequest: " + GroupRequest);
            String token = sharedPreferences.getString("auth_token", null);
            getDetailRequest(requestId, StatusRequest, token);
        }
    }

    private void getDetailRequest(int requestId, String StatusRequest, String token) {
        // Gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.requestDetailData(token, requestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            RequestDetailData requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(RequestDayOffDetail.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(RequestDayOffDetail.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    private void updateUserUI(RequestDetailData requestDetailData) {
        try {
            if (requestDetailData == null) {
                return;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String dataObject = gson.toJson(requestDetailData);
            Log.d("RequestLateEarlyDetail", "data detail: " + dataObject);

            if (requestDetailData.getRejectReason() == null) {
                reason_refused_request_layout.setVisibility(View.GONE);
            } else {
                reason_refused_request_layout.setVisibility(View.VISIBLE);
            }


            select_method_request.setText(requestDetailData.getRequestMethod().getName());
            etNgayBatDau.setText(requestDetailData.getStartDate());
            etNgayKetThuc.setText(requestDetailData.getEndDate());
            etGioBatDau.setText(requestDetailData.getStartTime());
            etGioKetThuc.setText(requestDetailData.getEndTime());
            txt_type_request.setText(requestDetailData.getRequestGroup().getName());
            txt_name_request.setText(requestDetailData.getRequestName());
            txt_reason_refused_request.setText(requestDetailData.getRejectReason());
            txt_name_employye_request.setText(requestDetailData.getEmployee().getName());
            txt_part_request.setText(requestDetailData.getDepartment().getName());

            txt_status_request.setText(requestDetailData.getStatus().getName());

            try {
                String colorCode = requestDetailData.getStatus().getColor(); // Lấy mã màu (dạng #cccccc)
                int color = Color.parseColor(colorCode); // Chuyển mã màu thành int

                // Đặt màu chữ
                txt_status_request.setTextColor(color);

                // Tạo màu nền nhạt hơn (thêm alpha)
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)); // Alpha = 50 (~20% độ trong suốt)

                // Đặt màu nền
                txt_status_request.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));

            } catch (IllegalArgumentException e) {
                // Xử lý trường hợp mã màu không hợp lệ
                Log.e("RequestAdapter", "Invalid color code: " + requestDetailData.getStatus().getColor());
                txt_status_request.setTextColor(Color.BLACK); // Đặt màu chữ mặc định (ví dụ: đen)
                txt_status_request.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY)); // Đặt màu nền mặc định (ví dụ: xám nhạt)
            }



            txt_reason_request.setText(requestDetailData.getReason());

            if(requestDetailData.getDirectManager() != null) {
                txt_manager_direct_request.setText(requestDetailData.getDirectManager().getName());
            } else {
                txt_manager_direct_request.setText("Chưa có quản lý trực tiếp");
            }



            List<Consignee> consigneeList = requestDetailData.getConsignee(); // Giả sử đây là danh sách Consignee
            if (consigneeList != null && !consigneeList.isEmpty()) {
                StringBuilder consigneeNames = new StringBuilder();

                // Lặp qua danh sách để lấy tên
                for (Consignee consignee : consigneeList) {
                    if (consignee != null && consignee.getName() != null) {
                        consigneeNames.append(consignee.getName()).append(" "); // Nối thêm khoảng trắng sau mỗi tên
                    }
                }
                // Gán chuỗi kết quả vào TextView
                txt_fixed_reviewer_request.setText(consigneeNames.toString().trim()); // trim() để loại bỏ khoảng trắng dư thừa ở cuối
            } else {
                // Trường hợp danh sách trống hoặc null
                txt_fixed_reviewer_request.setText("Không có người duyệt cố định"); // Thông báo mặc định
            }



            List<Follower> followerList = requestDetailData.getFollower(); // Giả sử đây là danh sách Consignee
            if (followerList != null && !followerList.isEmpty()) {
                StringBuilder followerNames = new StringBuilder();

                // Lặp qua danh sách để lấy tên
                for (Follower follower : followerList) {
                    if (follower != null && follower.getName() != null) {
                        followerNames.append(follower.getName()).append(" "); // Nối thêm khoảng trắng sau mỗi tên
                    }
                }
                txt_follower_request.setText(followerNames.toString().trim()); // trim() để loại bỏ khoảng trắng dư thừa ở cuối
            } else {
                // Trường hợp danh sách trống hoặc null
                txt_follower_request.setText("Không có người theo dõi"); // Thông báo mặc định
            }


            adapter = new ActionRequestDetailAdapter(requestDetailData.getApprovalLogs());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            // các nút hành động
            actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews(); // Xóa các button cũ (nếu có)
            List<ListStatus> listStatus = requestDetailData.getListStatus();
            if (listStatus != null && !listStatus.isEmpty()) {
                int totalItems = listStatus.size();
                for (int i = 0; i < totalItems; i++) {
                    ListStatus listStatus1 = listStatus.get(i);
                    AppCompatButton button = new AppCompatButton(this);

                    // Set text and normal case
                    button.setText(listStatus1.getName().toLowerCase());
                    button.setAllCaps(false);
                    button.setTextColor(Color.WHITE);
                    button.setPadding(24, 12, 24, 12); // Thêm padding

                    int buttonColor = Color.parseColor(listStatus1.getColor() != null ? listStatus1.getColor() : "#007BFF");
                    button.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                    button.setStateListAnimator(null);

                    // Căn các nút cách đều và sát hai bên
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1.0f // Chia đều khoảng trống
                    );
                    params.setMargins(4, 8, 4, 8); // Margin nhỏ đều hai bên

                    button.setLayoutParams(params);
                    button.setOnClickListener(v -> {
                        Log.d("ButtonClick", "Clicked on: " + listStatus1.getName());
                    });
                    actionButtonContainer.addView(button);
                }
            } else {
                Log.e("ButtonList", "No buttons found in API response");
            }



        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
        // Kiểm tra xem có fragment nào trong back stack không
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Nếu có, quay lại fragment trước đó
            getSupportFragmentManager().popBackStack();
        } else {
            // Nếu không có fragment nào trong back stack, gọi super để kết thúc activity
            super.onBackPressed();
        }
    }
}