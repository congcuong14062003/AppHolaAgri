package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.adapter.RequestMethodAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.ListStatus;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestLateEarlyDetail extends AppCompatActivity {
    TextView notice_lately_layout,  txt_type_request,select_method_request, txt_reason_refused_request, txt_name_employye_request, txt_part_request, txt_manager_direct_request,txt_fixed_reviewer_request,txt_follower_request;
    AppCompatButton txt_status_request;
    EditText txt_name_request, edtNgayBatDau, etDurationLateEarly, etNgayKetThuc, txt_reason_request;
    LinearLayout reason_refused_request_layout, actionButtonContainer;
    private RecyclerView recyclerView;
    private ActionRequestDetailAdapter adapter;
    ImageView backBtnReview, rbNhieuNgay_detail, rbMotNgay_detail, rbWork_detail, rbIndividual_detail;
    View   overlay_background;
    ConstraintLayout overlayFilterStatus;
    private ConstraintLayout overlay, overlay_filter_status_container;
    Integer requestId;
    String StatusRequest;
    Integer GroupRequest;
    private RequestDetailData requestDetailData; // Biến toàn cục
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_late_early_detail);
        txt_type_request = findViewById(R.id.txt_type_request);
        txt_name_request = findViewById(R.id.txt_name_request);
        txt_name_employye_request = findViewById(R.id.txt_name_employye_request);
        txt_part_request = findViewById(R.id.txt_part_request);
        txt_status_request = findViewById(R.id.txt_status_request_detail);
        edtNgayBatDau = findViewById(R.id.edtNgayBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
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
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);


        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        Intent intent = getIntent();
        if (intent != null) {
             requestId = intent.getIntExtra("requestId", -1); // Nhận requestId
             StatusRequest = intent.getStringExtra("StatusRequest");            // Nhận type
             GroupRequest = intent.getIntExtra("GroupRequest", -1);            // Nhận type
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

            Log.d("RequestLateEarlyDetail", "requestId: " + requestId);
            Log.d("RequestLateEarlyDetail", "StatusRequest: " + StatusRequest);
            Log.d("RequestLateEarlyDetail", "GroupRequest: " + GroupRequest);



            String token = sharedPreferences.getString("auth_token", null);
            getDetailRequest(requestId, StatusRequest, token);
//            if(StatusRequest.equals("Lưu nháp") || StatusRequest.equals("Từ chối")) {
//                getInitFormCreateRequest(token, GroupRequest);
//                txt_name_request.setEnabled(true);
//                txt_name_request.setBackgroundTintList(null); // Xóa background tint
//
//                select_method_request.setBackgroundTintList(null); // Xóa background tint
//
//                overlayFilterStatus = findViewById(R.id.overlay_filter_status);
//                Button buttonCloseOverlay = findViewById(R.id.button_close_overlay);
//                buttonCloseOverlay.setOnClickListener(v -> {
//                    overlayFilterStatus.setVisibility(View.GONE);
//                    overlay_background.setVisibility(View.GONE);
//                });
//                select_method_request.setOnClickListener(v -> {
//                    overlayFilterStatus.setVisibility(View.VISIBLE);
//                    overlay_background.setVisibility(View.VISIBLE);
//                });
//
//                overlay_background.setOnTouchListener((view, event) -> {
//                    // Check if the touch event is outside the overlay_filter_status_container
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        // Get the coordinates of the touch
//                        float x = event.getX();
//                        float y = event.getY();
//
//                        // Check if the touch is outside the overlay_filter_status_container
//                        int[] location = new int[2];
//                        overlay_filter_status_container.getLocationOnScreen(location);
//                        int containerX = location[0];
//                        int containerY = location[1];
//                        int containerWidth = overlay_filter_status_container.getWidth();
//                        int containerHeight = overlay_filter_status_container.getHeight();
//
//                        if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
//                            // Touch is outside the container, so hide the overlay
//                            overlay.setVisibility(View.GONE);
//                            overlay_background.setVisibility(View.GONE);
//                        }
//                    }
//                    return true; // Handle the touch event and prevent further propagation
//                });
//
//            }

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
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(RequestLateEarlyDetail.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(RequestLateEarlyDetail.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    private void getInitFormCreateRequest(String token, int GroupRequestId) {
        Log.d("CreateRequestDayOffActivity", "GroupRequestId: " + GroupRequestId);
        // Gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.initCreateRequest(token, GroupRequestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            requestDetailData = apiResponse.getData();
//                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("CreateRequestDayOffActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(RequestLateEarlyDetail.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestDayOffActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(RequestLateEarlyDetail.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
            // Lấy danh sách hình thức từ API
//            List<RequestMethod> listMethods = requestDetailData.getListMethod();
//            if (listMethods != null && !listMethods.isEmpty()) {
//                RecyclerView recyclerView = findViewById(R.id.recycler_filter_status_create);
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                RequestMethodAdapter adapter = new RequestMethodAdapter(listMethods, select_method_request, overlayFilterStatus, overlay_background, GroupRequest, selectedMethod -> {
//                    requestDetailData.setRequestMethod(selectedMethod);
//                    Log.d("SelectedMethod", "User selected: " + selectedMethod.getName());
//
//                });
//
//                recyclerView.setAdapter(adapter);
//            }


            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String dataObject = gson.toJson(requestDetailData);
            Log.d("RequestLateEarlyDetail", "data detail: " + dataObject);




            if(requestDetailData.getDateType() == 1) {
                rbNhieuNgay_detail.setImageResource(R.drawable.checked_radio);
            } else {
                rbMotNgay_detail.setImageResource(R.drawable.checked_radio);
            }


            if(requestDetailData.getType() == 2) {
                rbWork_detail.setImageResource(R.drawable.checked_radio);
            } else {
                rbIndividual_detail.setImageResource(R.drawable.checked_radio);
            }

            if(requestDetailData.getRequestMethod().getThreshold() != null) {
                if(requestDetailData.getDuration() > requestDetailData.getRequestMethod().getThreshold().getMinute()) {
                    notice_lately_layout.setText("Trừ 0.5 công do muộn > " + requestDetailData.getRequestMethod().getThreshold().getMinute() + " phút");
                } else {
                    notice_lately_layout.setText("");
                }
            } else {
                notice_lately_layout.setText("");

            }

            select_method_request.setText(requestDetailData.getRequestMethod().getName());
            edtNgayBatDau.setText(requestDetailData.getStartDate());
            etNgayKetThuc.setText(requestDetailData.getEndDate());
            etDurationLateEarly.setText("--" + requestDetailData.getDuration() + " phút--");
            txt_type_request.setText(requestDetailData.getRequestGroup().getName());
            txt_name_request.setText(requestDetailData.getRequestName());

            txt_name_employye_request.setText(requestDetailData.getEmployee().getName());
            txt_part_request.setText(requestDetailData.getDepartment().getName());

            txt_status_request.setText(requestDetailData.getStatus().getName());

            Log.d("RequestLateEarlyDetail", "vào chi tiết: ");


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

    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}