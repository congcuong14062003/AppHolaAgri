package com.example.appholaagri.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestDayOffActivity extends AppCompatActivity {
    EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create,
            edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create,
            edt_reason_request_create;
    TextView txt_type_request, select_method_request, tvThoiGianBatDau, tvThoiGianKetThuc;
    ImageView backBtnReview, rbMotNgayImage, rbNhieuNgayImage, rbIndividual_create, rbWork_create;
    // Khởi tạo các LinearLayout và ImageView
    View overlay_background;
    EditText etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc;
    private RequestDetailData requestDetailData; // Biến toàn cục
    private ConstraintLayout overlay, overlay_filter_status_container;
    ConstraintLayout overlayFilterStatus;
    private Integer GroupRequestType,GroupRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_day_off);

        // ánh xạ
        txt_type_request = findViewById(R.id.txt_type_request_create);
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        rbMotNgayImage = findViewById(R.id.rbMotNgay_create);
        rbNhieuNgayImage = findViewById(R.id.rbNhieuNgay_create);
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        backBtnReview = findViewById(R.id.backBtnReview_create);
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        select_method_request = findViewById(R.id.select_method_request);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        tvThoiGianBatDau = findViewById(R.id.tvThoiGianBatDau);
        tvThoiGianKetThuc = findViewById(R.id.tvThoiGianKetThuc);
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        etGioBatDau = findViewById(R.id.etGioBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        etGioKetThuc = findViewById(R.id.etGioKetThuc);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);


        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1); // Nhận requestId
        }
        if (GroupRequestId != null && token != null) {
            getInitFormCreateRequest(token, GroupRequestId);
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
        select_method_request.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.VISIBLE);
            overlay_background.setVisibility(View.VISIBLE);
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
                    overlay.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true; // Handle the touch event and prevent further propagation
        });

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDateStr = sdf.format(calendar.getTime());

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
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("CreateRequestDayOffActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestDayOffActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestDetailDataJson = gson.toJson(requestDetailData);

        try {
            if (requestDetailData == null) {
                return;
            }
            // Hiển thị thông tin cơ bản
            Log.d("CreateRequestDayOffActivity", "txt_type_request: " + requestDetailData.getRequestGroup().getName());

            txt_type_request.setText(requestDetailData.getRequestGroup().getName());
            edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            if (requestDetailData.getDirectManager() != null) {
                edt_manager_direct_request_create.setText(requestDetailData.getDirectManager().getName());
            } else {
                edt_manager_direct_request_create.setText("Chưa có quản lý trực tiếp");
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
                edt_fixed_reviewer_request_create.setText(consigneeNames.toString().trim()); // trim() để loại bỏ khoảng trắng dư thừa ở cuối
            } else {
                // Trường hợp danh sách trống hoặc null
                edt_fixed_reviewer_request_create.setText("Không có người duyệt cố định"); // Thông báo mặc định
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
                edt_follower_request_create.setText(followerNames.toString().trim()); // trim() để loại bỏ khoảng trắng dư thừa ở cuối
            } else {
                // Trường hợp danh sách trống hoặc null
                edt_follower_request_create.setText("Không có người theo dõi"); // Thông báo mặc định
            }
            // Lấy danh sách hình thức từ API
            List<RequestMethod> listMethods = requestDetailData.getListMethod();
            if (listMethods != null && !listMethods.isEmpty()) {
                RecyclerView recyclerView = findViewById(R.id.recycler_filter_status_create);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                RequestMethodAdapter adapter = new RequestMethodAdapter(listMethods, select_method_request, overlayFilterStatus, overlay_background, GroupRequestType, selectedMethod -> {
                    requestDetailData.setRequestMethod(selectedMethod);
                });

                recyclerView.setAdapter(adapter);
            }
            Log.d("CreateRequestDayOffActivity", "User selected: " + requestDetailData.getRequestMethod().getName());


            LinearLayout actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews(); // Xóa các button cũ (nếu có)

            List<ListStatus> listStatus = requestDetailData.getListStatus();
            if (listStatus != null && !listStatus.isEmpty()) {
                int totalItems = listStatus.size();
                for (int i = 0; i < totalItems; i++) {
                    ListStatus listStatus1 = listStatus.get(i);
                    AppCompatButton button = new AppCompatButton(this);
                    button.setText(listStatus1.getName());
                    button.setTextColor(Color.WHITE);

                    int buttonColor = Color.parseColor(listStatus1.getColor() != null ? listStatus1.getColor() : "#007BFF");
                    button.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
                    // Đặt background từ drawable
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                    // Thiết lập LayoutParams để căn đều
                    // Nếu có đúng 2 nút thì căn sát mép 2 bên
                    button.setStateListAnimator(null);
                    // Thiết lập LayoutParams
                    LinearLayout.LayoutParams params;
                    if (totalItems == 2) {
                        // Nếu có 2 nút, căn sát về hai bên nhưng vẫn có margin
                        params = new LinearLayout.LayoutParams(
                                0,  // Chiều rộng chiếm đều không gian
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f // Chia đều khoảng trống
                        );
                        params.setMargins(16, 8, 16, 8); // Thêm margin hai bên
                    } else {
                        // Nếu có 3 nút trở lên thì đặt margin bình thường
                        params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,  // Chiều rộng tự co giãn theo nội dung
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(8, 8, 8, 8); // Có margin đều hai bên
                    }
                    button.setLayoutParams(params);
                    button.setOnClickListener(v -> {
                        String nameInput = edt_name_request_create.getText().toString().trim();

//                        if (!nameInput.matches("^[\\p{L}0-9 ]+$")) {
//                            edt_name_request_create.setError("Chỉ nhập chữ cái có dấu và số!");
//                            return;
//                        }
//
//                        if (nameInput.length() > 150) {
//                            edt_name_request_create.setError("Tên không được vượt quá 150 ký tự!");
//                            return;
//                        }

                        Log.d("ButtonClick", "Clicked on: " + listStatus1.getName());
                        handleCreateRequest(requestDetailData, listStatus1);
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
    // tạo request
    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus1) {
        // Lấy dữ liệu từ giao diện nhập vào requestDetailData
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setStartTime(etGioBatDau.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());
        // validate
        if (requestDetailData.getRequestName().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập tên đề xuất!");
            return;
        }
            if (requestDetailData.getStartDate().isEmpty() || requestDetailData.getStartTime().isEmpty()) {
                CustomToast.showCustomToast(this, "Vui lòng chọn thời gian bắt đầu!");
                return;
            }
            if (requestDetailData.getEndDate().isEmpty() || requestDetailData.getEndTime().isEmpty()) {
                CustomToast.showCustomToast(this, "Vui lòng chọn thời gian kết thúc!");
                return;
            }
        if (requestDetailData.getReason().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập lý do!");
            return;
        }

        // Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
        if (!requestDetailData.getStartDate().isEmpty() && !requestDetailData.getEndDate().isEmpty()) {
            if (compareDates(requestDetailData.getStartDate(), requestDetailData.getEndDate()) > 0) {
                CustomToast.showCustomToast(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                return;
            }
        }

        // Kiểm tra nếu ngày bắt đầu == ngày kết thúc thì phải kiểm tra giờ
        if (!requestDetailData.getStartDate().isEmpty() && !requestDetailData.getEndDate().isEmpty() &&
                !requestDetailData.getStartTime().isEmpty() && !requestDetailData.getEndTime().isEmpty()) {
            if (requestDetailData.getStartDate().equals(requestDetailData.getEndDate())) {
                if (compareTimes(requestDetailData.getStartTime(), requestDetailData.getEndTime()) > 0) {
                    CustomToast.showCustomToast(this, "Khi ngày bắt đầu và ngày kết thúc trùng nhau, giờ kết thúc phải lớn hơn giờ bắt đầu!");
                    return;
                }
            }
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
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String dataObject = gson.toJson(groupRequestCreateRequest);
        Log.d("CreateRequestDayOffActivity", "data to createeeeee: " + dataObject);

            // đơn xin nghỉ phép
            Call<ApiResponse<String>> call = apiInterface.dayOffCreateRequest(token, groupRequestCreateRequest);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    Log.d("CreateRequest", "Response: " + response);
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                            startActivity(new Intent(CreateRequestDayOffActivity.this, RequestActivity.class));
                        } else {
                            CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
                }
            });
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