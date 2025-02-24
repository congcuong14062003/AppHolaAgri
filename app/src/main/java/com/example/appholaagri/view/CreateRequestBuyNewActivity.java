package com.example.appholaagri.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestBuyNewActivity extends AppCompatActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc,
            edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create;
    private TextView title_request, txt_type_request_create;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData; // Biến toàn cục
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;

    // over lay
    View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    ConstraintLayout overlayFilterStatus;
    private LinearLayout layout_action_history_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_buy_new);
// ánh xạ
        // container
        create_request_container = findViewById(R.id.create_request_container);
        // nút back
        backBtnReview = findViewById(R.id.backBtnReview_create);
        // tiêu đề
        title_request = findViewById(R.id.title_request);
        txt_status_request_detail = findViewById(R.id.txt_status_request_detail);

        // loại đề xuất
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        // tên đề xuất
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        // người đề xuất
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        // bộ phận
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        // ngày bắt đầu
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        // giờ bắt đầu
        etGioBatDau = findViewById(R.id.etGioBatDau);
        // ngày kết thúc
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        // giờ kết thúc
        etGioKetThuc = findViewById(R.id.etGioKetThuc);
        // lí do
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        // quản ly trực tiếp
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        // người duyệt cố định
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        // người theo dõi
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        layout_action_history_request = findViewById(R.id.layout_action_history_request);

        // over lay
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);

        recyclerViewApprovalLogs = findViewById(R.id.recyclerViewApprovalLogs);
        recyclerViewApprovalLogs.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1); // Nhận requestId
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestBuyNewActivity", "StatusRequest: " + StatusRequest);
        }

        if (StatusRequest > 2) {
            edt_name_request_create.setEnabled(false);
            edt_name_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            etNgayBatDau.setEnabled(false);
            etNgayBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            etGioBatDau.setEnabled(false);
            etGioBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            etNgayKetThuc.setEnabled(false);
            etNgayKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            etGioKetThuc.setEnabled(false);
            etGioKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

            edt_reason_request_create.setEnabled(false);
            edt_reason_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

        }
        // init
        layout_action_history_request.setVisibility(View.GONE);
        txt_status_request_detail.setVisibility(View.GONE);

        // Khởi tạo nếu null
        if (requestDetailData == null) {
            requestDetailData = new RequestDetailData();
            requestDetailData.setDuration(30);
        }

        if (requestId != -1) {
            create_request_container.setVisibility(View.GONE);
            txt_status_request_detail.setVisibility(View.VISIBLE);

            title_request.setText("Chi tiết đề xuất");
            getDetailRequest(requestId, token);
        } else {
            if (GroupRequestId != null && token != null) {
                Log.d("CreateRequestBuyNewActivity", "Vào");
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }


        // event
        Button buttonCloseOverlay = findViewById(R.id.button_close_overlay);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        // event
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        buttonCloseOverlay.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });


        overlay_background.setOnTouchListener((view, event) -> {
            // Check if the touch event is outside the overlay_filter_status_container
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Get the coordinates of the touch
                float x = event.getX();
                float y = event.getY();

                // Check if the touch is outside the overlay_filter_status_container
                int[] location = new int[2];
                overlay_filter_status_container.getLocationOnScreen(location);
                int containerX = location[0];
                int containerY = location[1];
                int containerWidth = overlay_filter_status_container.getWidth();
                int containerHeight = overlay_filter_status_container.getHeight();

                if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
                    // Touch is outside the container, so hide the overlay
                    overlayFilterStatus.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true; // Handle the touch event and prevent further propagation
        });


        // Lấy ngày hiện tại
        Calendar currentDate = Calendar.getInstance();


        // Chọn ngày & giờ bắt đầu
        etNgayBatDau.setOnClickListener(v -> showDatePicker(etNgayBatDau));
        etGioBatDau.setOnClickListener(v -> showTimePicker(etGioBatDau));

        // Chọn ngày & giờ kết thúc
        etNgayKetThuc.setOnClickListener(v -> showDatePicker(etNgayKetThuc));
        etGioKetThuc.setOnClickListener(v -> showTimePicker(etGioKetThuc));

    }


    // Hàm hiển thị DatePickerDialog
    private void showDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        if (!editText.getText().toString().isEmpty()) {
            try {
                Date selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .parse(editText.getText().toString());
                if (selectedDate != null) {
                    calendar.setTime(selectedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    editText.setText(selectedDate);

                    String startDate = etNgayBatDau.getText().toString();
                    String endDate = etNgayKetThuc.getText().toString();

                    // Kiểm tra ngày kết thúc
                    if (editText.getId() == R.id.etNgayKetThuc) {
                        if (!startDate.isEmpty() && compareDates(selectedDate, startDate) < 0) {
                            CustomToast.showCustomToast(this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
                            editText.setText(""); // Xóa nếu không hợp lệ
                            return;
                        }
                    }

                    // Kiểm tra ngày bắt đầu
                    if (editText.getId() == R.id.etNgayBatDau) {
                        if (!endDate.isEmpty() && compareDates(selectedDate, endDate) > 0) {
                            CustomToast.showCustomToast(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                            editText.setText(""); // Xóa nếu không hợp lệ
                            return;
                        }
                    }

                    // Nếu ngày bắt đầu == ngày kết thúc -> Kiểm tra giờ
                    if (!startDate.isEmpty() && !endDate.isEmpty() && compareDates(startDate, endDate) == 0) {
                        String startTime = etGioBatDau.getText().toString();
                        String endTime = etGioKetThuc.getText().toString();

                        if (!startTime.isEmpty() && !endTime.isEmpty() && compareTimes(startTime, endTime) > 0) {
                            CustomToast.showCustomToast(this, "Giờ kết thúc không được nhỏ hơn giờ bắt đầu!");
                            etGioKetThuc.setText(""); // Xóa giờ kết thúc nếu không hợp lệ
                        }
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    editText.setText(selectedTime);

                    // Nếu chọn giờ kết thúc, kiểm tra giờ bắt đầu
                    if (editText.getId() == R.id.etGioKetThuc) {
                        String startDate = etNgayBatDau.getText().toString();
                        String endDate = etNgayKetThuc.getText().toString();
                        String startTime = etGioBatDau.getText().toString();

                        if (!startDate.isEmpty() && startDate.equals(endDate) && !startTime.isEmpty()) {
                            if (compareTimes(selectedTime, startTime) < 0) {
                                CustomToast.showCustomToast(this, "Giờ kết thúc không thể nhỏ hơn giờ bắt đầu!");

                                editText.setText(""); // Xóa nếu không hợp lệ
                            }
                        }
                    }

                    // Nếu chọn giờ bắt đầu, kiểm tra giờ kết thúc
                    if (editText.getId() == R.id.etGioBatDau) {
                        String startDate = etNgayBatDau.getText().toString();
                        String endDate = etNgayKetThuc.getText().toString();
                        String endTime = etGioKetThuc.getText().toString();

                        if (!startDate.isEmpty() && startDate.equals(endDate) && !endTime.isEmpty()) {
                            if (compareTimes(selectedTime, endTime) > 0) {
                                CustomToast.showCustomToast(this, "Giờ bắt đầu không thể lớn hơn giờ kết thúc!");
                                editText.setText(""); // Xóa nếu không hợp lệ
                            }
                        }
                    }
                },
                hour, minute, true);

        timePickerDialog.show();
    }

    // Hàm so sánh 2 ngày (dd/MM/yyyy)
    private int compareDates(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.compareTo(d2); // Trả về <0 nếu d1 < d2, >0 nếu d1 > d2, =0 nếu bằng nhau
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // Hàm so sánh 2 giờ (HH:mm)
    private int compareTimes(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // init data
    private void getInitFormCreateRequest(String token, int GroupRequestId) {
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
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("CreateRequestBuyNewActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestBuyNewActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    // nếu có id lấy data chi tiết của nháp và từ chối
    private void getDetailRequest(int requestId, String token) {
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
                            create_request_container.setVisibility(View.VISIBLE);
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }

    // cập nhật giao diện
    private void updateUserUI(RequestDetailData requestDetailData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestDetailDataJson = gson.toJson(requestDetailData);
        Log.d("CreateRequestBuyNewActivity", "requestDetailDataJson: " + requestDetailDataJson);

        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestBuyNewActivity", "requestDetailData is null");
                return;
            }

            if (requestDetailData.getStatus() != null) {
                txt_status_request_detail.setText(requestDetailData.getStatus().getName());

                String colorCode = requestDetailData.getStatus().getColor(); // Lấy mã màu (dạng #cccccc)
                int color = Color.parseColor(colorCode); // Chuyển mã màu thành int

                // Đặt màu chữ
               txt_status_request_detail.setTextColor(color);

                // Tạo màu nền nhạt hơn (thêm alpha)
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)); // Alpha = 50 (~20% độ trong suốt)

                // Đặt màu nền
                txt_status_request_detail.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
            }

            // Kiểm tra từng thuộc tính trước khi gọi
            if (requestDetailData.getRequestGroup() != null && requestDetailData.getRequestGroup().getName() != null) {
                txt_type_request_create.setText(requestDetailData.getRequestGroup().getName());
            }

            if (requestDetailData.getRequestName() != null) {
                edt_name_request_create.setText(requestDetailData.getRequestName());
            }



            if (requestDetailData.getEmployee() != null && requestDetailData.getEmployee().getName() != null) {
                edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            }

            if (requestDetailData.getDepartment() != null && requestDetailData.getDepartment().getName() != null) {
                edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            }

            if (requestDetailData.getStartDate() != null) {
                etNgayBatDau.setText(requestDetailData.getStartDate());
            }

            if (requestDetailData.getEndDate() != null) {
                etNgayKetThuc.setText(requestDetailData.getEndDate());
            }

            if(requestDetailData.getStartTime() != null) {
                etGioBatDau.setText(requestDetailData.getStartTime());
            }

            if(requestDetailData.getEndTime() != null) {
                etGioKetThuc.setText(requestDetailData.getEndTime());
            }

            if (requestDetailData.getReason() != null) {
                edt_reason_request_create.setText(requestDetailData.getReason());
            }

            if (requestDetailData.getDirectManager() != null
                    && requestDetailData.getDirectManager().getName() != null) {
                edt_manager_direct_request_create.setText(requestDetailData.getDirectManager().getName());
            } else {
                edt_manager_direct_request_create.setText("Chưa có quản lý trực tiếp");
            }

            // Xử lý danh sách Consignee
            List<Consignee> consigneeList = requestDetailData.getConsignee();
            if (consigneeList != null && !consigneeList.isEmpty()) {
                StringBuilder consigneeNames = new StringBuilder();
                for (Consignee consignee : consigneeList) {
                    if (consignee != null && consignee.getName() != null) {
                        consigneeNames.append(consignee.getName()).append(" ");
                    }
                }
                edt_fixed_reviewer_request_create.setText(consigneeNames.toString().trim());
            } else {
                edt_fixed_reviewer_request_create.setText("Không có người duyệt cố định");
            }

            // Xử lý danh sách Follower
            List<Follower> followerList = requestDetailData.getFollower();
            if (followerList != null && !followerList.isEmpty()) {
                StringBuilder followerNames = new StringBuilder();
                for (Follower follower : followerList) {
                    if (follower != null && follower.getName() != null) {
                        followerNames.append(follower.getName()).append(" ");
                    }
                }
                edt_follower_request_create.setText(followerNames.toString().trim());
            } else {
                edt_follower_request_create.setText("Không có người theo dõi");
            }
            adapter = new ActionRequestDetailAdapter(requestDetailData.getApprovalLogs());
            recyclerViewApprovalLogs.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(requestDetailData.getApprovalLogs() != null && requestDetailData.getApprovalLogs().size() > 0) {
                layout_action_history_request.setVisibility(View.VISIBLE);
            }
            // Xử lý danh sách ListStatus
            List<ListStatus> listStatus = requestDetailData.getListStatus();
            LinearLayout actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews();
            if (listStatus != null && !listStatus.isEmpty()) {
                for (ListStatus status : listStatus) {
                    if (status != null && status.getName() != null) {
                        AppCompatButton button = new AppCompatButton(this);
                        button.setText(status.getName());
                        button.setTextColor(Color.WHITE);
                        button.setPadding(24, 12, 24, 12);

                        String color = (status.getColor() != null) ? status.getColor() : "#007BFF";
                        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                        button.setStateListAnimator(null);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        params.setMargins(4, 8, 4, 8);
                        button.setLayoutParams(params);

                        button.setOnClickListener(v ->
                                {
//                                    if(requestId == -1) {
                                    handleCreateRequest(requestDetailData, status);
//                                    }
                                }
                        );
                        actionButtonContainer.addView(button);
                    }
                }
            } else {
                Log.e("ButtonList", "listStatus is null or empty");
            }

        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage(), e);
        }
    }


    // tạo request
    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus1) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        // Lấy dữ liệu từ giao diện nhập vào requestDetailData
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setStartTime(etGioBatDau.getText().toString().trim());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());

        // validate
        if (requestDetailData.getRequestName().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập tên đề xuất!");
            return;
        }
        if (requestDetailData.getReason().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập lý do!");
            return;
        }


        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Tạo object để gửi request
        GroupRequestCreateRequest groupRequestCreateRequest = new GroupRequestCreateRequest();
        groupRequestCreateRequest.setContact(requestDetailData.getContact());
        groupRequestCreateRequest.setDateType(requestDetailData.getDateType());
        groupRequestCreateRequest.setDuration(requestDetailData.getDuration());
        groupRequestCreateRequest.setEndDate(requestDetailData.getEndDate());
        groupRequestCreateRequest.setEndTime(requestDetailData.getEndTime());
        groupRequestCreateRequest.setReason(requestDetailData.getReason());
        groupRequestCreateRequest.setRejectReason("");

        // Gán requestGroup
        GroupRequestCreateRequest.RequestGroup requestGroup = new GroupRequestCreateRequest.RequestGroup(
                requestDetailData.getRequestGroup().getCode(),
                requestDetailData.getRequestGroup().getId(),
                requestDetailData.getRequestGroup().getName(),
                requestDetailData.getRequestGroup().getStatus()
        );
        groupRequestCreateRequest.setRequestGroup(requestGroup);

        // Gán requestMethod
        if (requestDetailData.getRequestMethod() != null) {
            GroupRequestCreateRequest.RequestMethod requestMethod = new GroupRequestCreateRequest.RequestMethod(
                    requestDetailData.getRequestMethod().getCode(),
                    requestDetailData.getRequestMethod().getId(),
                    requestDetailData.getRequestMethod().getName(),
                    requestDetailData.getRequestMethod().getStatus()
            );
            groupRequestCreateRequest.setRequestMethod(requestMethod);
        }


        // Gán status
        GroupRequestCreateRequest.Status status = new GroupRequestCreateRequest.Status(
                listStatus1.getCode(),
                listStatus1.getId(),
                listStatus1.getName(),
                listStatus1.getStatus()
        );
        groupRequestCreateRequest.setStatus(status);

        groupRequestCreateRequest.setRequestId(requestDetailData.getRequestId());
        groupRequestCreateRequest.setRequestName(requestDetailData.getRequestName());
        groupRequestCreateRequest.setStartDate(requestDetailData.getStartDate());
        groupRequestCreateRequest.setStartTime(requestDetailData.getStartTime());
        groupRequestCreateRequest.setType(requestDetailData.getType());
        // Tạo JSON log để kiểm tra dữ liệu

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.dayOffCreateRequest(token, groupRequestCreateRequest);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                        if (apiResponse.getStatus() == 200) {
                            startActivity(new Intent(CreateRequestBuyNewActivity.this, RequestActivity.class));
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
                }
            });
        } else {
            if (groupRequestCreateRequest.getStatus().getId() == 2) {
                showRejectReasonDialog(apiInterface, token, requestDetailData, groupRequestCreateRequest);
            } else {
                sendModifyRequest(apiInterface, token, groupRequestCreateRequest);
            }
        }

    }
    private void showRejectReasonDialog(ApiInterface apiInterface, String token, RequestDetailData requestDetailData, GroupRequestCreateRequest groupRequestCreateRequest) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reject_reason, null);
        builder.setView(dialogView);

        EditText etReason = dialogView.findViewById(R.id.etReason);
        AppCompatButton btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        AppCompatButton btn_confirm = dialogView.findViewById(R.id.btn_confirm);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Thiết lập ban đầu: vô hiệu hóa nút xác nhận
        btn_confirm.setEnabled(false);
        btn_confirm.setTextColor(ContextCompat.getColor(this, R.color.secondarycolor));
        btn_confirm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e8f7f2")));

        // Lắng nghe thay đổi nội dung nhập
        etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = s.toString().trim().isEmpty();
                btn_confirm.setEnabled(!isEmpty);
                btn_confirm.setTextColor(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.secondarycolor : R.color.white));
                btn_confirm.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.white : R.color.secondarycolor)));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý khi nhấn nút xác nhận
        btn_confirm.setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (!reason.isEmpty()) {
                requestDetailData.setRejectReason(reason);
                groupRequestCreateRequest.setRejectReason(reason);
                sendModifyRequest(apiInterface, token, groupRequestCreateRequest);
                dialog.dismiss();
            } else {
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Vui lòng nhập lý do");
            }
        });

        // Ẩn bàn phím khi chạm ngoài EditText
        dialogView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        findViewById(android.R.id.content).setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        btn_cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void sendModifyRequest(ApiInterface apiInterface, String token, GroupRequestCreateRequest groupRequestCreateRequest) {
        Call<ApiResponse<String>> call = apiInterface.modifyRequest(token, groupRequestCreateRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        startActivity(new Intent(CreateRequestBuyNewActivity.this, RequestActivity.class));
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }

}