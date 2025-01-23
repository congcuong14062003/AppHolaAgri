package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.adapter.SalaryTableAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDetailActivity extends BaseActivity {
    TextView txt_type_request, txt_reason_refused_request, txt_name_request, txt_name_employye_request, txt_part_request, txt_reason_request, txt_manager_direct_request,txt_fixed_reviewer_request,txt_follower_request;

    AppCompatButton txt_status_request;
    LinearLayout reason_refused_request_layout;
    EditText etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc;
    private RecyclerView recyclerView;
    private ActionRequestDetailAdapter adapter;
    ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_detail);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        Intent intent = getIntent();
        if (intent != null) {
            Integer requestId = intent.getIntExtra("requestId", -1); // Nhận requestId
            Integer typeRequest = intent.getIntExtra("typeRequest", -1);            // Nhận type
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            getDetailRequest(requestId, typeRequest, token);
        }
    }
    private void getDetailRequest(int requestId, int typeRequest, String token) {
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
                        CustomToast.showCustomToast(RequestDetailActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(RequestDetailActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
            if (requestDetailData.getRejectReason() == null) {
                reason_refused_request_layout.setVisibility(View.GONE);
            } else {
                reason_refused_request_layout.setVisibility(View.VISIBLE);
            }
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


            // Cập nhật Spinner với giá trị requestMethod
            Spinner spinnerHinhThuc = findViewById(R.id.spinnerHinhThuc);
            ArrayList<String> spinnerItems = new ArrayList<>();
            if (requestDetailData.getRequestMethod() != null) {
                spinnerItems.add(requestDetailData.getRequestMethod().getName()); // Thêm tên vào Spinner
            }

            // Nếu bạn muốn thêm nhiều item cho spinner, bạn có thể thay thế mảng ở đây
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHinhThuc.setAdapter(spinnerAdapter);

            // Kiểm tra giá trị dateTypeName và cập nhật trạng thái của RadioButton
            String dateTypeName = requestDetailData.getDateTypeName(); // Giả sử đây là trường lưu giá trị "Nhiều ngày" hoặc "1 ngày"
            ImageView rbMotNgay = findViewById(R.id.rbMotNgay);
            ImageView rbNhieuNgay = findViewById(R.id.rbNhieuNgay);

            // Log ra giá trị của dateTypeName
            Log.d("RequestDetailActivity", "dateTypeName: " + dateTypeName);

            // Kiểm tra nếu dateTypeName không null và so sánh
            if (dateTypeName != null) {
                if (dateTypeName.equals("Nhiều ngày")) {
                    rbNhieuNgay.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio)); // Chọn RadioButton "Nhiều ngày"
                    rbMotNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio)); // Bỏ chọn RadioButton "1 ngày" nếu có
                } else if (dateTypeName.equals("1 ngày")) {
                    rbMotNgay.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio)); // Chọn RadioButton "1 ngày"
                    rbNhieuNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio)); // Bỏ chọn RadioButton "Nhiều ngày" nếu có
                } else {
                    // Nếu không có giá trị hợp lệ, có thể bỏ cả hai chọn
                    rbMotNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio));
                    rbNhieuNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio));
                }
            } else {
                // Nếu dateTypeName là null, bỏ cả hai chọn
                rbMotNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio));
                rbNhieuNgay.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio));
            }
            // ngày bắt đầu
            String ngayBatDau = requestDetailData.getStartDate(); // Định dạng: "dd/MM/yyyy"
            String gioBatDau = requestDetailData.getStartTime(); // Định dạng: "HH:mm"
            String ngayKetThuc = requestDetailData.getEndDate(); // Định dạng: "dd/MM/yyyy"
            String gioKetThuc = requestDetailData.getEndTime(); // Định dạng: "HH:mm"
            etNgayBatDau.setText(ngayBatDau);
            etGioBatDau.setText(gioBatDau);
            etNgayKetThuc.setText(ngayKetThuc);
            etGioKetThuc.setText(gioKetThuc);
            txt_reason_request.setText(requestDetailData.getReason());
            txt_manager_direct_request.setText(requestDetailData.getDirectManager().getName());
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

            txt_follower_request.setText(requestDetailData.getDirectManager().getName());

            adapter = new ActionRequestDetailAdapter(requestDetailData.getApprovalLogs());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();



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