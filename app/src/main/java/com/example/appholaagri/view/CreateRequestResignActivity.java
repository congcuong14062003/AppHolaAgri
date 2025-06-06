package com.example.appholaagri.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
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
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.ListStatus;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
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
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestResignActivity extends BaseActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc,
            edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create;
    private TextView title_request, txt_type_request_create, edt_number_of_day_notices;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData;
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;
    private View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    private ConstraintLayout overlayFilterStatus;
    private LinearLayout layout_action_history_request;
    private Dialog loadingDialog;
    private SwitchCompat switchUrgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_resign);

        // Ánh xạ
        create_request_container = findViewById(R.id.create_request_container);
        backBtnReview = findViewById(R.id.backBtnReview_create);
        title_request = findViewById(R.id.title_request);
        switchUrgent = findViewById(R.id.switch_urgent);
        txt_status_request_detail = findViewById(R.id.txt_status_request_detail);
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        etGioBatDau = findViewById(R.id.etGioBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        etGioKetThuc = findViewById(R.id.etGioKetThuc);
        edt_number_of_day_notices = findViewById(R.id.edt_number_of_day_notices);
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        layout_action_history_request = findViewById(R.id.layout_action_history_request);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        recyclerViewApprovalLogs = findViewById(R.id.recyclerViewApprovalLogs);
        recyclerViewApprovalLogs.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1);
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1);
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestResignActivity", "GroupRequestType: " + GroupRequestType);
            Log.d("CreateRequestResignActivity", "StatusRequest: " + StatusRequest);
        }



        layout_action_history_request.setVisibility(View.GONE);
        txt_status_request_detail.setVisibility(View.GONE);

        if (requestId != -1) {
            create_request_container.setVisibility(View.GONE);
            txt_status_request_detail.setVisibility(View.VISIBLE);
            title_request.setText("Chi tiết đề xuất");
            getDetailRequest(requestId, token);
        } else {
            if (GroupRequestId != null && token != null) {
                Log.d("CreateRequestResignActivity", "Vào");
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }

        // Sự kiện
        Button buttonCloseOverlay = findViewById(R.id.button_close_overlay);
        backBtnReview.setOnClickListener(view -> finish());
        buttonCloseOverlay.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });

        overlay_background.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();
                int[] location = new int[2];
                overlay_filter_status_container.getLocationOnScreen(location);
                int containerX = location[0];
                int containerY = location[1];
                int containerWidth = overlay_filter_status_container.getWidth();
                int containerHeight = overlay_filter_status_container.getHeight();
                if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
                    overlayFilterStatus.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true;
        });

        etGioBatDau.setOnClickListener(v -> showTimePicker(etGioBatDau));
        etGioKetThuc.setOnClickListener(v -> showTimePicker(etGioKetThuc));

        switchUrgent.setChecked(false);
        switchUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requestDetailData.setIsUrgent(isChecked ? 1 : 0);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String currentDate = sdf.format(calendar.getTime());
        etNgayBatDau.setText(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String nextDate = sdf.format(calendar.getTime());
        etNgayKetThuc.setText(nextDate);
        updateNoticeDays();
        edt_number_of_day_notices.setText("1");

        etNgayBatDau.setOnClickListener(v -> showDatePicker(etNgayBatDau, true));
        etNgayKetThuc.setOnClickListener(v -> showDatePicker(etNgayKetThuc, false));
    }

    private void showDatePicker(EditText editText, boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (!editText.getText().toString().isEmpty()) {
            try {
                Date selectedDate = sdf.parse(editText.getText().toString());
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
                    String selectedDateStr = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    editText.setText(selectedDateStr);

                    if (isStartDate) {
                        updateNoticeDays();
                        try {
                            String endDateStr = etNgayKetThuc.getText().toString();
                            if (!endDateStr.isEmpty()) {
                                Date startDate = sdf.parse(selectedDateStr);
                                Date endDate = sdf.parse(endDateStr);
                                if (startDate != null && endDate != null && endDate.compareTo(startDate) <= 0) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(startDate);
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    etNgayKetThuc.setText(sdf.format(cal.getTime()));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String startDateStr = etNgayBatDau.getText().toString();
                            if (!startDateStr.isEmpty()) {
                                Date startDate = sdf.parse(startDateStr);
                                Date endDate = sdf.parse(selectedDateStr);
                                if (startDate != null && endDate != null && endDate.compareTo(startDate) <= 0) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(startDate);
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    etNgayKetThuc.setText(sdf.format(cal.getTime()));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateNoticeDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            String startDateStr = etNgayBatDau.getText().toString();
            if (!startDateStr.isEmpty()) {
                Calendar currentCal = Calendar.getInstance();
                String currentDateStr = sdf.format(currentCal.getTime());
                Date currentDate = sdf.parse(currentDateStr);
                Date startDate = sdf.parse(startDateStr);

                if (currentDate != null && startDate != null) {
                    long diffInMillis = startDate.getTime() - currentDate.getTime();
                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis) + 1;
                    if (diffInDays >= 0) {
                        edt_number_of_day_notices.setText(String.valueOf(diffInDays));
                    } else {
                        etNgayBatDau.setText(currentDateStr);
                        edt_number_of_day_notices.setText("1");
                    }
                } else {
                    edt_number_of_day_notices.setText("1");
                }
            } else {
                edt_number_of_day_notices.setText("1");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            edt_number_of_day_notices.setText("1");
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
                },
                hour, minute, true);

        timePickerDialog.show();
    }

    private int compareDates(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.compareTo(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

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
                        } else {
                            CustomToast.showCustomToast(CreateRequestResignActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        Log.e("CreateRequestResignActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestResignActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestResignActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                Log.e("ApiHelper", t.getMessage());
            }
        });
    }

    private void getDetailRequest(int requestId, String token) {
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
                        Log.e("CreateRequestResignActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestResignActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestResignActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                Log.e("ApiHelper", t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUserUI(RequestDetailData requestDetailData) {
        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestResignActivity", "requestDetailData is null");
                return;
            }

            if (requestDetailData.getStatus() != null) {
                txt_status_request_detail.setText(requestDetailData.getStatus().getName());
                String colorCode = requestDetailData.getStatus().getColor();
                int color = Color.parseColor(colorCode);
                txt_status_request_detail.setTextColor(color);
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
                txt_status_request_detail.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                StatusRequest = requestDetailData.getStatus().getId();
                if (StatusRequest > 1) {
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
                    edt_number_of_day_notices.setEnabled(false);
                    edt_number_of_day_notices.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_reason_request_create.setEnabled(false);
                    edt_reason_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                }
            }

            if (requestDetailData.getRequestGroup() != null && requestDetailData.getRequestGroup().getName() != null) {
                txt_type_request_create.setText(requestDetailData.getRequestGroup().getName());
            }

            if (requestDetailData.getRequestName() != null) {
                edt_name_request_create.setText(requestDetailData.getRequestName());
            }

            if (requestDetailData.getIsUrgent() != null) {
                switchUrgent.setChecked(requestDetailData.getIsUrgent() == 1);
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

            if (requestDetailData.getStartTime() != null) {
                etGioBatDau.setText(requestDetailData.getStartTime());
            }

            if (requestDetailData.getEndTime() != null) {
                etGioKetThuc.setText(requestDetailData.getEndTime());
            }

            if (requestDetailData.getDuration() != null) {
                edt_number_of_day_notices.setText(requestDetailData.getDuration().toString());
            }

            if (requestDetailData.getReason() != null) {
                edt_reason_request_create.setText(requestDetailData.getReason());
            }

            if (requestDetailData.getDirectManager() != null && requestDetailData.getDirectManager().getName() != null) {
                edt_manager_direct_request_create.setText(requestDetailData.getDirectManager().getName());
            } else {
                edt_manager_direct_request_create.setText("Chưa có quản lý trực tiếp");
            }

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
                edt_fixed_reviewer_request_create.setText("Không có người duyệt chọn");
            }

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
            if (requestDetailData.getApprovalLogs() != null && !requestDetailData.getApprovalLogs().isEmpty()) {
                layout_action_history_request.setVisibility(View.VISIBLE);
            }

            List<ListStatus> listStatus = requestDetailData.getListStatus();
            LinearLayout actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews();
            if (listStatus != null && !listStatus.isEmpty()) {
                for (ListStatus status : listStatus) {
                    if (status != null && status.getName() != null) {
                        AppCompatButton button = new AppCompatButton(this);
                        button.setText(status.getName());
                        button.setTextColor(Color.WHITE);
                        button.setPadding(24, 10, 24, 10);
                        String color = (status.getColor() != null) ? status.getColor() : "#007BFF";
                        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                        button.setStateListAnimator(null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        params.setMargins(10, 8, 10, 8);
                        button.setLayoutParams(params);
                        button.setOnClickListener(v -> {
                            RequestDetailData.Status statusObj = new RequestDetailData.Status(
                                    status.getId(),
                                    status.getCode(),
                                    status.getName(),
                                    status.getStatus(),
                                    status.getColor(),
                                    status.getIndex()
                            );
                            requestDetailData.setStatus(statusObj);
                            handleCreateRequest(requestDetailData, status);
                        });
                        actionButtonContainer.addView(button);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CreateRequestResignActivity", "Error updating UI: " + e.getMessage(), e);
        }
    }

    private void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingDialog.setContentView(R.layout.dialog_loading);
            loadingDialog.setCancelable(false);
            if (loadingDialog.getWindow() != null) {
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
        loadingDialog.show();
    }

    private void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus1) {
        showLoading();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setStartTime(etGioBatDau.getText().toString().trim());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());

        String durationText = edt_number_of_day_notices.getText().toString().trim();
        if (!durationText.isEmpty()) {
            try {
                requestDetailData.setDuration(Double.parseDouble(durationText));
            } catch (NumberFormatException e) {
                CustomToast.showCustomToast(this, "Số ngày báo trước không hợp lệ");
                hideLoading();
                return;
            }
        } else {
            requestDetailData.setDuration(0.0);
        }

        if (requestDetailData.getRequestName().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập tên đề xuất!");
            hideLoading();
            return;
        }
        if (requestDetailData.getReason().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập lý do!");
            hideLoading();
            return;
        }
        if (requestDetailData.getStartDate().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng chọn ngày làm việc cuối cùng!");
            hideLoading();
            return;
        }
        if (requestDetailData.getEndDate().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng chọn ngày chấm dứt hợp đồng!");
            hideLoading();
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        String jsonResponse = gson.toJson(requestDetailData);
        Log.d("CreateRequestResignActivity", "Data gửi lên: " + jsonResponse);

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.resignCreateRequestNew(token, requestDetailData);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestResignActivity.this, apiResponse.getMessage());
                            Intent intent = new Intent(CreateRequestResignActivity.this, HomeActivity.class);
                            intent.putExtra("navigate_to", "newsletter");
                            startActivity(intent);
                            finish();
                        } else {
                            CustomToast.showCustomToast(CreateRequestResignActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi: " + t.getMessage());
                }
            });
        } else {
            if (listStatus1.getId() == 2) {
                showRejectReasonDialog(apiInterface, token, requestDetailData);
            } else {
                sendModifyRequest(apiInterface, token, requestDetailData);
            }
        }
    }

    private void showRejectReasonDialog(ApiInterface apiInterface, String token, RequestDetailData requestDetailData) {
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

        btn_confirm.setEnabled(false);
        btn_confirm.setTextColor(ContextCompat.getColor(this, R.color.secondarycolor));
        btn_confirm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e8f7f2")));

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

        btn_confirm.setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (!reason.isEmpty()) {
                requestDetailData.setRejectReason(reason);
                sendModifyRequest(apiInterface, token, requestDetailData);
                dialog.dismiss();
            } else {
                CustomToast.showCustomToast(CreateRequestResignActivity.this, "Vui lòng nhập lý do");
            }
        });

        dialogView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        findViewById(android.R.id.content).setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        btn_cancel.setOnClickListener(view -> {
            hideLoading();
            dialog.dismiss();
        });

        dialog.setOnDismissListener(dialogInterface -> hideLoading());
        dialog.show();
    }

    private void sendModifyRequest(ApiInterface apiInterface, String token, RequestDetailData requestDetailData) {
        showLoading();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Log.d("CreateRequestResignActivity", "Data chỉnh sửa: " + gson.toJson(requestDetailData));

        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestResignActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        Intent intent = new Intent(CreateRequestResignActivity.this, HomeActivity.class);
                        intent.putExtra("navigate_to", "newsletter");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestResignActivity.this, "Lỗi: " + t.getMessage());
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
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}