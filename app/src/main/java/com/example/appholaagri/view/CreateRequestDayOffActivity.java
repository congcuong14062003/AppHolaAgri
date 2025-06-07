package com.example.appholaagri.view;

import android.app.Activity;
import android.app.AlertDialog;
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

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestDayOffActivity extends BaseActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, edt_duration, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc,
            edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create;
    private TextView title_request, txt_type_request_create, select_method_request;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData;
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;
    private Dialog loadingDialog;
    private View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    private ConstraintLayout overlayFilterStatus;
    private LinearLayout layout_action_history_request;
    private SwitchCompat switchUrgent;
    private static final int REQUEST_CODE_FOLLOWER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_day_off);

        create_request_container = findViewById(R.id.create_request_container);
        backBtnReview = findViewById(R.id.backBtnReview_create);
        title_request = findViewById(R.id.title_request);
        txt_status_request_detail = findViewById(R.id.txt_status_request_detail);
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        switchUrgent = findViewById(R.id.switch_urgent);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        select_method_request = findViewById(R.id.select_method_request);
        edt_duration = findViewById(R.id.edt_duration);
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        etGioBatDau = findViewById(R.id.etGioBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        etGioKetThuc = findViewById(R.id.etGioKetThuc);
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        layout_action_history_request = findViewById(R.id.layout_action_history_request);
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
            Log.d("CreateRequestDayOffActivity", "StatusRequest: " + StatusRequest);
        }



        layout_action_history_request.setVisibility(View.GONE);
        txt_status_request_detail.setVisibility(View.GONE);

        if (requestId != -1) {
            create_request_container.setVisibility(View.GONE);
            txt_status_request_detail.setVisibility(View.VISIBLE);
            title_request.setText("Chi tiết đề xuất");
            getDetailRequest(requestId, token);
        } else if (GroupRequestId != null && token != null) {
            Log.d("CreateRequestDayOffActivity", "Vào");
            getInitFormCreateRequest(token, GroupRequestId);
        }

        Button buttonCloseOverlay = findViewById(R.id.button_close_overlay);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        backBtnReview.setOnClickListener(view -> finish());
        buttonCloseOverlay.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });
        select_method_request.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.VISIBLE);
            overlay_background.setVisibility(View.VISIBLE);
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

        Calendar currentDate = Calendar.getInstance();
        etNgayBatDau.setOnClickListener(v -> showDatePicker(etNgayBatDau));
        etGioBatDau.setOnClickListener(v -> showTimePicker(etGioBatDau));
        etNgayKetThuc.setOnClickListener(v -> showDatePicker(etNgayKetThuc));
        etGioKetThuc.setOnClickListener(v -> showTimePicker(etGioKetThuc));

        switchUrgent.setChecked(false);
        switchUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requestDetailData.setIsUrgent(isChecked ? 1 : 0);
        });

        edt_duration.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.equals(current)) {
                    edt_duration.removeTextChangedListener(this);

                    // Validate input: allow decimal numbers with up to two decimal places
                    if (!isValidDecimal(input)) {
                        s.replace(0, s.length(), current);
                    } else {
                        current = input;
                    }

                    edt_duration.addTextChangedListener(this);
                }
            }

            private boolean isValidDecimal(String input) {
                if (input.isEmpty() || input.equals(".")) return true;
                // Allow numbers like 0.5, 1.23, 10.50
                return Pattern.matches("^\\d*\\.?\\d{0,2}$", input);
            }
        });


        // Trong hàm onCreate, sự kiện click cho edt_follower_request_create
        edt_follower_request_create.setOnClickListener(view -> {
            Intent intent1 = new Intent(CreateRequestDayOffActivity.this, SelectFollowerActivity.class);
            // Truyền danh sách người theo dõi hiện tại
            if (requestDetailData != null && requestDetailData.getFollower() != null) {
                intent1.putExtra("current_followers", new ArrayList<>(requestDetailData.getFollower()));
            } else {
                intent1.putExtra("current_followers", new ArrayList<Follower>());
            }
            startActivityForResult(intent1, REQUEST_CODE_FOLLOWER);
        });

    }

    // Sửa onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLLOWER && resultCode == RESULT_OK && data != null) {
            // Lấy danh sách người theo dõi đã chọn
            ArrayList<Follower> selectedFollowers = (ArrayList<Follower>) data.getSerializableExtra("selected_followers");
            if (selectedFollowers != null) {
                try {
                    // Cập nhật requestDetailData
                    if (requestDetailData == null) {
                        requestDetailData = new RequestDetailData();
                    }
                    // Thêm @ vào name của các Follower nếu chưa có
                    for (Follower follower : selectedFollowers) {
                        if (follower != null && follower.getName() != null && !follower.getName().startsWith("@")) {
                            follower.setName("@" + follower.getName());
                        }
                    }
                    requestDetailData.setFollower(selectedFollowers);

                    // Cập nhật giao diện EditText
                    StringBuilder followerNames = new StringBuilder();
                    for (Follower follower : selectedFollowers) {
                        if (follower != null && follower.getName() != null) {
                            followerNames.append(follower.getName()).append(", ");
                        }
                    }
                    String followerText = followerNames.length() > 0 ? followerNames.substring(0, followerNames.length() - 2) : "Không có người theo dõi";
                    edt_follower_request_create.setText(followerText);

                    Log.d("CreateRequestLateEarlyActivity", "Selected followers updated: " + followerText);
                } catch (Exception e) {
                    Log.e("CreateRequestLateEarlyActivity", "Error processing followers: " + e.getMessage());
                    CustomToast.showCustomToast(this, "Lỗi khi cập nhật người theo dõi.");
                }
            } else {
                // Nếu không có người theo dõi được chọn
                requestDetailData.setFollower(new ArrayList<>());
                edt_follower_request_create.setText("Không có người theo dõi");
                Log.w("CreateRequestLateEarlyActivity", "Selected followers is null");
            }
        }
    }



    private void showDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        if (!editText.getText().toString().isEmpty()) {
            try {
                Date selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(editText.getText().toString());
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
                    if (editText.getId() == R.id.etNgayKetThuc) {
                        if (!startDate.isEmpty() && compareDates(selectedDate, startDate) < 0) {
                            CustomToast.showCustomToast(this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
                            editText.setText("");
                            return;
                        }
                    }
                    if (editText.getId() == R.id.etNgayBatDau) {
                        if (!endDate.isEmpty() && compareDates(selectedDate, endDate) > 0) {
                            CustomToast.showCustomToast(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
                            editText.setText("");
                            return;
                        }
                    }
                    if (!startDate.isEmpty() && !endDate.isEmpty() && compareDates(startDate, endDate) == 0) {
                        String startTime = etGioBatDau.getText().toString();
                        String endTime = etGioKetThuc.getText().toString();
                        if (!startTime.isEmpty() && !endTime.isEmpty() && compareTimes(startTime, endTime) > 0) {
                            CustomToast.showCustomToast(this, "Giờ kết thúc không được nhỏ hơn giờ bắt đầu!");
                            etGioKetThuc.setText("");
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
                    if (editText.getId() == R.id.etGioKetThuc) {
                        String startDate = etNgayBatDau.getText().toString();
                        String endDate = etNgayKetThuc.getText().toString();
                        String startTime = etGioBatDau.getText().toString();
                        if (!startDate.isEmpty() && startDate.equals(endDate) && !startTime.isEmpty()) {
                            if (compareTimes(selectedTime, startTime) < 0) {
                                CustomToast.showCustomToast(this, "Giờ kết thúc không thể nhỏ hơn giờ bắt đầu!");
                                editText.setText("");
                            }
                        }
                    }
                    if (editText.getId() == R.id.etGioBatDau) {
                        String startDate = etNgayBatDau.getText().toString();
                        String endDate = etNgayKetThuc.getText().toString();
                        String endTime = etGioKetThuc.getText().toString();
                        if (!startDate.isEmpty() && startDate.equals(endDate) && !endTime.isEmpty()) {
                            if (compareTimes(selectedTime, endTime) > 0) {
                                CustomToast.showCustomToast(this, "Giờ bắt đầu không thể lớn hơn giờ kết thúc!");
                                editText.setText("");
                            }
                        }
                    }
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
                            CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        Log.e("CreateRequestDayOffActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestDayOffActivity", "Error during response handling: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
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
                            Log.d("CreateRequestDayOffActivity", "requestDetailDataJson: " + "vào");
                            create_request_container.setVisibility(View.VISIBLE);
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("CreateRequestDayOffActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestDayOffActivity", "Error during response handling: " + e.getMessage());
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
        Log.d("CreateRequestDayOffActivity", "requestDetailDataJson: " + requestDetailDataJson);

        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestDayOffActivity", "requestDetailData is null");
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
                    select_method_request.setEnabled(false);
                    select_method_request.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_duration.setEnabled(false);
                    edt_duration.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
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

                    edt_follower_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_follower_request_create.setEnabled(false);
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
            if (requestDetailData.getRequestMethod() != null && requestDetailData.getRequestMethod().getName() != null) {
                select_method_request.setText(requestDetailData.getRequestMethod().getName());
            }
            if (requestDetailData.getEmployee() != null && requestDetailData.getEmployee().getName() != null) {
                edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            }
            if (requestDetailData.getDepartment() != null && requestDetailData.getDepartment().getName() != null) {
                edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            }
            if (requestDetailData.getDuration() != null) {
                // Format to two decimal places
                String durationText = String.format(Locale.US, "%.2f", requestDetailData.getDuration());
                edt_duration.setText(durationText);
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
                edt_fixed_reviewer_request_create.setText("Không có người duyệt cố định");
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
                        button.setPadding(24, 12, 24, 12);
                        String color = (status.getColor() != null) ? status.getColor() : "#007BFF";
                        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                        button.setStateListAnimator(null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        params.setMargins(4, 8, 4, 8);
                        button.setLayoutParams(params);
                        button.setOnClickListener(v -> {
                            // Chuyển ListStatus thành Status và gán vào requestDetailData
                            RequestDetailData.Status statusObj = new  RequestDetailData.Status(
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
            } else {
                Log.e("ButtonList", "listStatus is null or empty");
            }
        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage(), e);
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
        String durationText = edt_duration.getText().toString().trim();
        if (!durationText.isEmpty()) {
            try {
                double duration = Double.parseDouble(durationText);
                // Round to two decimal places
                duration = Math.round(duration * 100.0) / 100.0;
                requestDetailData.setDuration(duration);
            } catch (NumberFormatException e) {
                CustomToast.showCustomToast(this, "Số ngày nghỉ không hợp lệ");
                hideLoading();
                return;
            }
        } else {
            requestDetailData.setDuration(0.0);
        }
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setStartTime(etGioBatDau.getText().toString().trim());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());


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

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        String jsonResponse = gson.toJson(requestDetailData);
        Log.d("CreateRequestDayOffActivity", "Data gửi lên: " + jsonResponse);

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.dayOffCreateRequestNew(token, requestDetailData);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                            Intent intent = new Intent(CreateRequestDayOffActivity.this, HomeActivity.class);
                            intent.putExtra("navigate_to", "newsletter");
                            startActivity(intent);
                            finish();
                        } else {
                            CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
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
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Vui lòng nhập lý do");
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
        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        Intent intent = new Intent(CreateRequestDayOffActivity.this, HomeActivity.class);
                        intent.putExtra("navigate_to", "newsletter");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
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