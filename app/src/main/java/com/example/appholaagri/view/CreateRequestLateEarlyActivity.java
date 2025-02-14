package com.example.appholaagri.view;

import android.app.AlertDialog;
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
import android.widget.NumberPicker;
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
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestLateEarlyActivity extends AppCompatActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, etNgayBatDau, etNgayKetThuc,
            edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create,
            etDurationLateEarly;
    private TextView title_request, txt_type_request_create, select_method_request, notice_lately_layout;
    private ImageView backBtnReview, rbMotNgay_create, rbNhieuNgay_create, rbIndividual_create, rbWork_create;
    private LinearLayout tvThoiGianKetThuc_layout;
    private RequestDetailData requestDetailData; // Biến toàn cục
    private Integer GroupRequestType, GroupRequestId, requestId;


    // over lay
    View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    ConstraintLayout overlayFilterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_late_early);
        // ánh xạ
        // nút back
        backBtnReview = findViewById(R.id.backBtnReview_create);
        // tiêu đề
        title_request = findViewById(R.id.title_request);
        // loại đề xuất
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        // tên đề xuất
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        // người đề xuất
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        // bộ phận
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        // hình thức
        select_method_request = findViewById(R.id.select_method_request);
        // mốc thời gian 1 ngày
        rbMotNgay_create = findViewById(R.id.rbMotNgay_create);
        // mốc thời gian nhiều ngày
        rbNhieuNgay_create = findViewById(R.id.rbNhieuNgay_create);
        // ngày bắt đầu
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        // duration
        etDurationLateEarly = findViewById(R.id.etDurationLateEarly);
        // ngày kết thúc
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        // layout ngày kết thúc
        tvThoiGianKetThuc_layout = findViewById(R.id.tvThoiGianKetThuc_layout);
        // lí do cá nhân
        rbIndividual_create = findViewById(R.id.rbIndividual_create);
        // li do công việc
        rbWork_create = findViewById(R.id.rbWork_create);
        // lí do
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        // quản ly trực tiếp
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        // người duyệt cố định
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        // người theo dõi
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        // thông báo đi muộn, về sớm quá
        notice_lately_layout = findViewById(R.id.notice_lately_layout);
        // over lay
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);


        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1); // Nhận requestId
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestLateEarlyActivity", "requestId: " + requestId);
        }
        // init
        tvThoiGianKetThuc_layout.setVisibility(View.GONE);
        rbMotNgay_create.setImageResource(R.drawable.checked_radio);
        rbIndividual_create.setImageResource(R.drawable.checked_radio);
        // Khởi tạo nếu null
        if (requestDetailData == null) {
            requestDetailData = new RequestDetailData();
            requestDetailData.setDuration(30);
        }
        etDurationLateEarly.setText("--" + requestDetailData.getDuration() + " phút--");


        if (requestId != -1) {
            getDetailRequest(requestId, token);
        } else {
            if (GroupRequestId != null && token != null) {
                Log.d("CreateRequestLateEarlyActivity", "Vào");
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }


        // event

        etDurationLateEarly.setOnClickListener(v -> showMinutePicker());

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
                    overlayFilterStatus.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true; // Handle the touch event and prevent further propagation
        });


        // Lấy ngày hiện tại
        Calendar currentDate = Calendar.getInstance();

        // Mặc định ngày hiện tại cho etNgayBatDau
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etNgayBatDau.setText(sdf.format(currentDate.getTime()));

        // Chọn ngày bắt đầu
        etNgayBatDau.setOnClickListener(v -> showDatePicker(etNgayBatDau, currentDate));

        // Chọn ngày kết thúc
        etNgayKetThuc.setOnClickListener(v -> showDatePicker(etNgayKetThuc, currentDate));

        rbNhieuNgay_create.setOnClickListener(v -> {
            rbNhieuNgay_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbMotNgay_create.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"
            tvThoiGianKetThuc_layout.setVisibility(View.VISIBLE);
            requestDetailData.setDateType(1);
            // Xử lý thêm logic cho lựa chọn "Nhiều ngày"
            Log.d("Selection", "Nhiều ngày được chọn");
        });
        // Sau khi chọn ngày kết thúc, tính số ngày báo trước

        rbMotNgay_create.setOnClickListener(v -> {
            rbMotNgay_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbNhieuNgay_create.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"
            tvThoiGianKetThuc_layout.setVisibility(View.GONE);
            etNgayKetThuc.setText("");
            requestDetailData.setDateType(0);
            // Xử lý thêm logic cho lựa chọn "1 ngày"
            Log.d("Selection", "1 ngày được chọn");
        });

        // Đặt sự kiện click cho "lí do cá nhân"
        rbIndividual_create.setOnClickListener(v -> {
            rbIndividual_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbWork_create.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"
            requestDetailData.setType(1);
        });
        // Đặt sự kiện click cho "lí do công việc"
        rbWork_create.setOnClickListener(v -> {
            rbWork_create.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbIndividual_create.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"
            requestDetailData.setType(2);
        });

    }

    // chọn số phút đi muộn về sớm
    private void showMinutePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn số phút");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(360);

        // Luôn đặt giá trị mặc định là 30 phút nếu requestDetailData null
        int defaultValue = (requestDetailData != null && requestDetailData.getDuration() > 0)
                ? requestDetailData.getDuration()
                : 30;

        numberPicker.setValue(defaultValue);

        numberPicker.setWrapSelectorWheel(true);
        layout.addView(numberPicker);
        builder.setView(layout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int selectedMinute = numberPicker.getValue();
            etDurationLateEarly.setText("--" + selectedMinute + " phút--");

            if (requestDetailData == null) {
                requestDetailData = new RequestDetailData();
            }
            requestDetailData.setDuration(selectedMinute);

            if (requestDetailData != null && requestDetailData.getRequestMethod() != null
                    && requestDetailData.getRequestMethod().getThreshold() != null) {
                if (selectedMinute > requestDetailData.getRequestMethod().getThreshold().getMinute()) {
                    notice_lately_layout.setText("Trừ 0.5 công do muộn > "
                            + requestDetailData.getRequestMethod().getThreshold().getMinute() + " phút");
                } else {
                    notice_lately_layout.setText("");
                }
            } else {
                Log.e("CreateRequestLateEarly", "requestMethod hoặc threshold bị null");
            }

        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Hàm hiển thị DatePickerDialog
    private void showDatePicker(EditText targetEditText, Calendar startDate) {
        Calendar calendar = (Calendar) startDate.clone();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    targetEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Nếu chọn ngày bắt đầu
        if (targetEditText == etNgayBatDau) {
            Calendar minDate = (Calendar) startDate.clone();
            minDate.add(Calendar.DAY_OF_MONTH, -3); // 3 ngày trước
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

            Calendar maxDate = (Calendar) startDate.clone();
            maxDate.add(Calendar.DAY_OF_MONTH, 3); // 3 ngày sau
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        // Nếu chọn ngày kết thúc
        if (targetEditText == etNgayKetThuc) {
            if (!etNgayBatDau.getText().toString().isEmpty()) {
                try {
                    Date startDateObj = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(etNgayBatDau.getText().toString());
                    if (startDateObj != null) {
                        datePickerDialog.getDatePicker().setMinDate(startDateObj.getTime() + 86400000); // Ngày sau ngày bắt đầu
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        datePickerDialog.show();
    }

    // init data
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
                        CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestDayOffActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
        Log.d("CreateRequestLateEarlyActivity", "requestDetailDataJson: " + requestDetailDataJson);

        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestLateEarlyActivity", "requestDetailData is null");
                return;
            }
            if (requestId != -1) {
                etDurationLateEarly.setText("--" + requestDetailData.getDuration() + " phút--");
            }
            // Kiểm tra từng thuộc tính trước khi gọi
            if (requestDetailData.getRequestGroup() != null && requestDetailData.getRequestGroup().getName() != null) {
                txt_type_request_create.setText(requestDetailData.getRequestGroup().getName());
            }
            Log.d("CreateRequestLateEarlyActivity", "Vào 123");

            if (requestDetailData.getRequestName() != null) {
                edt_name_request_create.setText(requestDetailData.getRequestName());
            }

            if (requestDetailData.getRequestMethod() != null && requestDetailData.getRequestMethod().getName() != null) {
                select_method_request.setText(requestDetailData.getRequestMethod().getName());
            }

            if (requestDetailData.getEmployee() != null && requestDetailData.getEmployee().getName() != null) {
                edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            }

            if (requestDetailData.getDepartment() != null && requestDetailData.getDepartment().getName() != null) {
                edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            }

            if (requestDetailData.getDateType() == 1) {
                rbNhieuNgay_create.setImageResource(R.drawable.checked_radio);
                rbMotNgay_create.setImageResource(R.drawable.unchecked_radio);
                tvThoiGianKetThuc_layout.setVisibility(View.VISIBLE);
            } else {
                rbMotNgay_create.setImageResource(R.drawable.checked_radio);
                rbNhieuNgay_create.setImageResource(R.drawable.unchecked_radio);
            }

            if (requestDetailData.getType() == 2) {
                rbWork_create.setImageResource(R.drawable.checked_radio);
                rbIndividual_create.setImageResource(R.drawable.unchecked_radio);
            } else {
                rbIndividual_create.setImageResource(R.drawable.checked_radio);
                rbWork_create.setImageResource(R.drawable.unchecked_radio);
            }

            if (requestDetailData.getRequestMethod() != null && requestDetailData.getRequestMethod().getThreshold() != null) {
                int minute = requestDetailData.getRequestMethod().getThreshold().getMinute();

                int duration = requestDetailData.getDuration(); // Giả sử getDuration() trả về int
                if (duration > 0 && duration > minute) {
                    notice_lately_layout.setText("Trừ 0.5 công do muộn > " + minute + " phút");
                } else {
                    notice_lately_layout.setText("");
                }
            } else {
                notice_lately_layout.setText("");
                Log.e("CreateRequestActivity", "RequestMethod hoặc Threshold bị null");
            }


            if (requestDetailData.getStartDate() != null) {
                etNgayBatDau.setText(requestDetailData.getStartDate());
            }

            if (requestDetailData.getEndDate() != null) {
                etNgayKetThuc.setText(requestDetailData.getEndDate());
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

            // Xử lý danh sách RequestMethod
            List<RequestMethod> listMethods = requestDetailData.getListMethod();
            if (listMethods != null && !listMethods.isEmpty()) {
                RecyclerView recyclerView = findViewById(R.id.recycler_filter_status_create);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                RequestMethodAdapter adapter = new RequestMethodAdapter(
                        listMethods,
                        select_method_request,
                        overlayFilterStatus,
                        overlay_background,
                        GroupRequestType,
                        selectedMethod -> requestDetailData.setRequestMethod(selectedMethod)
                );
                recyclerView.setAdapter(adapter);

                // Nếu có requestId, đặt selectedItem từ API
                if (requestId != -1 && requestDetailData.getRequestMethod() != null) {
                    int selectedId = requestDetailData.getRequestMethod().getId();
                    adapter.setSelectedMethodById(selectedId);
                    recyclerView.scrollToPosition(
                            IntStream.range(0, listMethods.size())
                                    .filter(i -> listMethods.get(i).getId() == selectedId)
                                    .findFirst()
                                    .orElse(0)
                    );
                }
            } else {
                Log.e("CreateRequestActivity", "listMethods is null or empty");
            }

            // Xử lý danh sách ListStatus
            List<ListStatus> listStatus = requestDetailData.getListStatus();
            LinearLayout actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews();
            if (listStatus != null && !listStatus.isEmpty()) {
                for (ListStatus status : listStatus) {
                    if (status != null && status.getName() != null) {
                        AppCompatButton button = new AppCompatButton(this);
                        button.setText(status.getName().toLowerCase());
                        button.setAllCaps(false);
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
                                    if(requestId == -1) {
                                        handleCreateRequest(requestDetailData, status);
                                    }
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
        // Đặt giá trị mặc định cho duration là 30 nếu chưa có
        if (requestDetailData.getDuration() == 0) {
            requestDetailData.setDuration(30);
        }
        etDurationLateEarly.setText("--" + requestDetailData.getDuration() + " phút--");
        // Lấy dữ liệu từ giao diện nhập vào requestDetailData
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String dataObject = gson.toJson(requestDetailData);
        Log.d("CreateRequestDayOffActivity", "data to createeeeee: " + dataObject);


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
        String dataObjects = gson.toJson(groupRequestCreateRequest);
//        Log.d("CreateRequestDayOffActivity", "data to createeeeee: " + dataObjects);
//        Log.d("GroupRequestType: ", "GroupRequestType: " + GroupRequestType);

        // đơn xin nghỉ phép
        Call<ApiResponse<String>> call = apiInterface.lateEarlyCreateRequest(token, groupRequestCreateRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                Log.d("CreateRequest", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, apiResponse.getMessage());
                        startActivity(new Intent(CreateRequestLateEarlyActivity.this, RequestActivity.class));
                    } else {
                        CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestLateEarlyActivity.this, "Lỗi: " + t.getMessage());
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