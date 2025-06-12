package com.example.appholaagri.view;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.adapter.AttachmentRequestAdapter;
import com.example.appholaagri.adapter.CustomSpinnerAdapterCompany;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.appholaagri.utils.Utils;
import com.squareup.picasso.Picasso;

public class CreateRequestWorkReportActivity extends AppCompatActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create,
            edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create,
            edt_work_location, edt_purpose_request_create, edt_content_request_create, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc,
            edt_totalCost_request_create, edt_totalCost_text_request_create;
    private TextView title_request, txt_type_request_create, select_method_request;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData;
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;
    private LinearLayout layout_action_history_request;


    private View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    private ConstraintLayout overlayFilterStatus;
    private Dialog loadingDialog;
    private SwitchCompat switchUrgent;
    private RecyclerView recyclerViewAttachments;
    private AttachmentRequestAdapter attachmentAdapter;
    private boolean hasMethodSelected;
    private static final int REQUEST_CODE_FOLLOWER = 100;
    private Spinner spinner_company_request_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_work_report);

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
        select_method_request = findViewById(R.id.select_method_request);
        // công ty
        spinner_company_request_create = findViewById(R.id.spinner_company_request_create); // Ánh xạ Spinner

        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        layout_action_history_request = findViewById(R.id.layout_action_history_request);
        recyclerViewApprovalLogs = findViewById(R.id.recyclerViewApprovalLogs);
        recyclerViewApprovalLogs.setLayoutManager(new LinearLayoutManager(this));

        edt_work_location = findViewById(R.id.edt_work_location);
        edt_purpose_request_create = findViewById(R.id.edt_purpose_request_create);
        edt_content_request_create = findViewById(R.id.edt_content_request_create);
        // ngày bắt đầu
        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        // giờ bắt đầu
        etGioBatDau = findViewById(R.id.etGioBatDau);
        // ngày kết thúc
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        // giờ kết thúc
        etGioKetThuc = findViewById(R.id.etGioKetThuc);

        edt_totalCost_request_create = findViewById(R.id.edt_totalCost_request_create);
        edt_totalCost_text_request_create = findViewById(R.id.edt_totalCost_text_request_create);

        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        // Ánh xạ RecyclerView cho attachments
        recyclerViewAttachments = findViewById(R.id.recyclerViewAttachments);
        recyclerViewAttachments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // Lấy dữ liệu từ Intent
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1);
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1);
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestWorkReportActivity", "requestId: " + requestId);
        }
        // Khởi tạo Spinner cho công ty
        setupCompanySpinner();
        // Khởi tạo
        if (StatusRequest != null && StatusRequest > 1) {
            edt_name_request_create.setEnabled(false);
            edt_name_request_create.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#dee0df")));
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
                getInitFormCreateRequest(token, GroupRequestId);
            }
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


        // Sự kiện
        backBtnReview.setOnClickListener(view -> finish());

        // Thiết lập switch khẩn cấp
        switchUrgent.setChecked(false);
        switchUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (requestDetailData != null) {
                requestDetailData.setIsUrgent(isChecked ? 1 : 0);
            }
        });

        // Trong hàm onCreate, sự kiện click cho edt_follower_request_create
        edt_follower_request_create.setOnClickListener(view -> {
            Intent intent1 = new Intent(CreateRequestWorkReportActivity.this, SelectFollowerActivity.class);
            // Truyền danh sách người theo dõi hiện tại
            if (requestDetailData != null && requestDetailData.getFollower() != null) {
                intent1.putExtra("current_followers", new ArrayList<>(requestDetailData.getFollower()));
            } else {
                intent1.putExtra("current_followers", new ArrayList<Follower>());
            }
            startActivityForResult(intent1, REQUEST_CODE_FOLLOWER);
        });


        // Thêm TextWatcher để chuyển số thành chữ
        edt_totalCost_request_create.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edt_totalCost_request_create.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[^0-9]", "");
                    if (!cleanString.isEmpty()) {
                        try {
                            int parsed = Integer.parseInt(cleanString);
                            String formatted = Utils.formatNumberWithCommas(parsed);
                            current = formatted;
                            edt_totalCost_request_create.setText(formatted);
                            edt_totalCost_request_create.setSelection(formatted.length());

                            String amountInWords = convertNumberToWords(parsed);
                            edt_totalCost_text_request_create.setText(amountInWords + " đồng");
                        } catch (NumberFormatException e) {
                            edt_totalCost_text_request_create.setText("Số tiền không hợp lệ");
                        }
                    } else {
                        current = "";
                        edt_totalCost_text_request_create.setText("");
                    }

                    edt_totalCost_request_create.addTextChangedListener(this);
                }
            }
        });
    }

    private void setupCompanySpinner() {
        if (requestDetailData == null) {
            requestDetailData = new RequestDetailData();
        }

        List<RequestDetailData.CompanyList> companyList = requestDetailData.getCompanyList();
        if (companyList == null) {
            companyList = new ArrayList<>();
            requestDetailData.setCompanyList(companyList);
        }

        // Tạo adapter tùy chỉnh
        CustomSpinnerAdapterCompany adapter = new CustomSpinnerAdapterCompany(this, companyList);
        spinner_company_request_create.setAdapter(adapter);

        // Thiết lập công ty mặc định
        RequestDetailData.Company defaultCompany = requestDetailData.getCompany();
        int defaultPosition = -1;
        if (defaultCompany != null && defaultCompany.getId() != 0) {
            for (int i = 0; i < companyList.size(); i++) {
                if (companyList.get(i) != null && companyList.get(i).getId() == defaultCompany.getId()) {
                    defaultPosition = i;
                    spinner_company_request_create.setSelection(i);
                    adapter.setSelectedPosition(i);
                    break;
                }
            }
        }

        // Xử lý sự kiện chọn công ty
        List<RequestDetailData.CompanyList> finalCompanyList = companyList;
        spinner_company_request_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestDetailData.CompanyList selectedCompany = finalCompanyList.get(position);
                RequestDetailData.Company company = new RequestDetailData.Company();
                company.setId(selectedCompany.getId());
                company.setName(selectedCompany.getName());
                requestDetailData.setCompany(company);
                adapter.setSelectedPosition(position); // Cập nhật vị trí được chọn
                Log.d("CreateRequestLateEarlyActivity", "Selected company: " + selectedCompany.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không chọn
            }
        });
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

    // Hàm chuyển số thành chữ (tiếng Việt)
    private String convertNumberToWords(int number) {
        if (number == 0) return "không";

        String[] units = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] teens = {"mười", "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm", "mười sáu", "mười bảy", "mười tám", "mười chín"};
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
        String[] thousands = {"", "nghìn", "triệu", "tỷ"};

        String words = "";
        int group = 0;

        if (number < 0) {
            words = "âm ";
            number = -number;
        }

        do {
            int chunk = number % 1000;
            if (chunk != 0) {
                String chunkWords = convertThreeDigits(chunk, units, teens, tens);
                words = chunkWords + " " + thousands[group] + " " + words;
            }
            number /= 1000;
            group++;
        } while (number > 0);

        return words.trim().replaceAll(" +", " ");
    }

    private String convertThreeDigits(int number, String[] units, String[] teens, String[] tens) {
        String words = "";
        int hundred = number / 100;
        int remainder = number % 100;
        int ten = remainder / 10;
        int unit = remainder % 10;

        if (hundred > 0) {
            words += units[hundred] + " trăm";
            if (remainder > 0) {
                words += " ";
            }
        }

        if (remainder > 0) {
            if (remainder < 10) {
                words += units[unit];
            } else if (remainder < 20) {
                words += teens[unit];
            } else {
                words += tens[ten];
                if (unit > 0) {
                    words += " " + units[unit];
                }
            }
        }

        return words.trim();
    }


    // Sửa onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLLOWER && resultCode == RESULT_OK && data != null) {
            // Lấy danh sách người theo dõi đã chọn
            ArrayList<Follower> selectedFollowers = (ArrayList<Follower>) data.getSerializableExtra("selected_followers");
            boolean callModifyApi = data.getBooleanExtra("call_modify_api", false);

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

                    // Nếu có cờ call_modify_api, gọi API chỉnh sửa với status.id = -1
                    if (callModifyApi && requestId != -1) {
                        // Cập nhật status.id = -1
                        RequestDetailData.Status status = new RequestDetailData.Status(-1, null, null, 0, null, null);
                        requestDetailData.setStatus(status);

                        // Gọi API chỉnh sửa
                        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        String token = sharedPreferences.getString("auth_token", null);
                        if (token != null) {
                            ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
                            sendModifyRequestFollower(apiInterface, token, requestDetailData);
                        } else {
                            CustomToast.showCustomToast(this, "Không tìm thấy token. Vui lòng đăng nhập lại.");
                        }
                    }
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


    private void getInitFormCreateRequest(String token, int GroupRequestId) {
        showLoading();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.initCreateRequest(token, GroupRequestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                hideLoading();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        } else {
                            CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestWorkReportActivity", "Error: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void getDetailRequest(int requestId, String token) {
        showLoading();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.requestDetailData(token, requestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                hideLoading();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            create_request_container.setVisibility(View.VISIBLE);
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        } else {
                            CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestWorkReportActivity", "Error: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void updateUserUI(RequestDetailData requestDetailData) {
        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestWorkReportActivity", "requestDetailData is null");
                return;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String requestDetailDataJson = gson.toJson(requestDetailData);
            Log.d("CreateRequestWorkReportActivity", "dataa chi tiết: " + requestDetailDataJson);
            // Trạng thái
            if (requestDetailData.getStatus() != null && requestDetailData.getStatus().getName() != null) {
                txt_status_request_detail.setText(requestDetailData.getStatus().getName());
                String colorCode = requestDetailData.getStatus().getColor() != null ? requestDetailData.getStatus().getColor() : "#cccccc";
                int color = Color.parseColor(colorCode);
                txt_status_request_detail.setTextColor(color);
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
                txt_status_request_detail.setBackgroundTintList(android.content.res.ColorStateList.valueOf(backgroundColor));
                if (StatusRequest > 1) {
                    edt_name_request_create.setEnabled(false);
                    edt_name_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    select_method_request.setEnabled(false);
                    select_method_request.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    etNgayBatDau.setEnabled(false);
                    etNgayBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    etGioBatDau.setEnabled(false);
                    etGioBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    etNgayKetThuc.setEnabled(false);
                    etNgayKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    etGioKetThuc.setEnabled(false);
                    etGioKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));


                    edt_work_location.setEnabled(false);
                    edt_work_location.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_purpose_request_create.setEnabled(false);
                    edt_purpose_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_content_request_create.setEnabled(false);
                    edt_content_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_totalCost_request_create.setEnabled(false);
                    edt_totalCost_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    edt_totalCost_text_request_create.setEnabled(false);
                    edt_totalCost_text_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
                    spinner_company_request_create.setEnabled(false);
                    spinner_company_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

//                    edt_follower_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
//                    edt_follower_request_create.setEnabled(false);
                }
            }

            // Nhóm đề xuất
            if (requestDetailData.getRequestGroup() != null && requestDetailData.getRequestGroup().getName() != null) {
                txt_type_request_create.setText(requestDetailData.getRequestGroup().getName());
            }

            // Tên đề xuất
            if (requestDetailData.getRequestName() != null) {
                edt_name_request_create.setText(requestDetailData.getRequestName());
            }

            // Khẩn cấp
            if (requestDetailData.getIsUrgent() != null) {
                switchUrgent.setChecked(requestDetailData.getIsUrgent() == 1);
            }

            // Người đề xuất
            if (requestDetailData.getEmployee() != null && requestDetailData.getEmployee().getName() != null) {
                edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            }

            // Bộ phận
            if (requestDetailData.getDepartment() != null && requestDetailData.getDepartment().getName() != null) {
                edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            }
            // Cập nhật danh sách công ty và công ty mặc định
            if (requestDetailData.getCompanyList() != null && !requestDetailData.getCompanyList().isEmpty()) {
                setupCompanySpinner(); // Gọi lại để cập nhật Spinner với dữ liệu mới
            }
            if (requestDetailData.getRequestMethod() != null && requestDetailData.getRequestMethod().getName() != null) {
                select_method_request.setText(requestDetailData.getRequestMethod().getName());
            }

            if (requestDetailData.getBusinessTripFormReq() != null) {
                edt_work_location.setText(requestDetailData.getBusinessTripFormReq().getDestination());
                edt_content_request_create.setText(requestDetailData.getBusinessTripFormReq().getContent());
                int totalCost = requestDetailData.getBusinessTripFormReq().getTotalCost();
                edt_totalCost_request_create.setText(Utils.formatNumberWithCommas(totalCost));
                edt_totalCost_text_request_create.setText(requestDetailData.getBusinessTripFormReq().getTotalCostText());
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
                edt_purpose_request_create.setText(requestDetailData.getReason());
            }


            // Xử lý danh sách file đính kèm
            List<RequestDetailData.FileAttachment> fileAttachments = requestDetailData.getFileAttachment();
            if (fileAttachments != null && !fileAttachments.isEmpty()) {
                attachmentAdapter = new AttachmentRequestAdapter(this, fileAttachments, this::showImageDetailDialog);
                recyclerViewAttachments.setAdapter(attachmentAdapter);
                recyclerViewAttachments.setVisibility(View.VISIBLE);
            } else {
                recyclerViewAttachments.setVisibility(View.GONE);
            }


            // Quản lý trực tiếp
            if (requestDetailData.getDirectManager() != null && requestDetailData.getDirectManager().getName() != null) {
                edt_manager_direct_request_create.setText(requestDetailData.getDirectManager().getName());
            } else {
                edt_manager_direct_request_create.setText("Chưa có quản lý trực tiếp");
            }

            // Người duyệt cố định
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

            // Người theo dõi
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


            // Lịch sử hành động
            if (requestDetailData.getApprovalLogs() != null && !requestDetailData.getApprovalLogs().isEmpty()) {
                adapter = new ActionRequestDetailAdapter(requestDetailData.getApprovalLogs());
                recyclerViewApprovalLogs.setAdapter(adapter);
                layout_action_history_request.setVisibility(View.VISIBLE);
            }

            // Nút hành động
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
                        String color = status.getColor() != null ? status.getColor() : "#007BFF";
                        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor(color)));
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                        button.setStateListAnimator(null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        params.setMargins(4, 8, 4, 8);
                        button.setLayoutParams(params);
                        button.setOnClickListener(v -> {
                            // Chuyển ListStatus thành Status và gán vào requestDetailData
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
            Log.e("CreateRequestWorkReportActivity", "Error updating UI: " + e.getMessage());
        }
    }


    private void showImageDetailDialog(RequestDetailData.FileAttachment attachment) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image_detail);

        ImageView imageView = dialog.findViewById(R.id.image_detail);
        TextView textFileName = dialog.findViewById(R.id.text_file_name);
        ImageView btnClose = dialog.findViewById(R.id.btn_close_dialog);

        // Hiển thị tên file
        textFileName.setText(attachment.getName());

        // Load ảnh nếu là file hình ảnh
        if (attachment.getPath() != null && isImageFile(attachment.getPath())) {
            Picasso.get()
                    .load(attachment.getPath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(imageView);
        } else {
            // Hiển thị thông báo hoặc icon mặc định cho file không phải ảnh
            imageView.setImageResource(R.drawable.avatar);
            CustomToast.showCustomToast(this, "File không phải ảnh, không thể hiển thị chi tiết.");
        }

        // Đóng dialog khi nhấn nút close
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Ngăn đóng dialog khi chạm ngoài
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    private boolean isImageFile(String path) {
        String lowerPath = path.toLowerCase();
        return lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg") || lowerPath.endsWith(".png") || lowerPath.endsWith(".gif");
    }


    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus) {
        showLoading();
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());

        // Cập nhật status
        //        if (listStatus != null) {
        //            RequestDetailData.Status status = new RequestDetailData.Status(
        //                    listStatus.getId(),
        //                    listStatus.getCode(),
        //                    listStatus.getName(),
        //                    listStatus.getStatus()
        //            );
        //            requestDetailData.setStatus(status);
        //        }

        // Tạo hoặc lấy instance BusinessTripFormReq
        RequestDetailData.BusinessTripFormReq businessTripFormReq = requestDetailData.getBusinessTripFormReq();
        if (businessTripFormReq == null) {
            businessTripFormReq = new RequestDetailData.BusinessTripFormReq();
        }

        // Gán các giá trị từ EditText vào BusinessTripFormReq
        businessTripFormReq.setDestination(edt_work_location.getText().toString().trim()); // Địa điểm công tác
        businessTripFormReq.setContent(edt_purpose_request_create.getText().toString().trim()); // Mục đích
        businessTripFormReq.setContent(edt_content_request_create.getText().toString().trim()); // Nội dung (có thể cần kiểm tra trùng lặp với purpose)
        try {
            String formattedCost = edt_totalCost_request_create.getText().toString().trim();
            int totalCost = (int) Utils.parseNumberFromFormattedString(formattedCost);
            businessTripFormReq.setTotalCost(totalCost); // Tổng chi phí (số nguyên)
        } catch (NumberFormatException e) {
            businessTripFormReq.setTotalCost(0); // Giá trị mặc định nếu không hợp lệ
        }
        businessTripFormReq.setTotalCostText(edt_totalCost_text_request_create.getText().toString().trim()); // Văn bản tổng chi phí

        // Gán BusinessTripFormReq vào requestDetailData
        requestDetailData.setBusinessTripFormReq(businessTripFormReq);


        requestDetailData.setEndDate(etNgayKetThuc.getText().toString().trim());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString().trim());
        requestDetailData.setStartTime(etGioBatDau.getText().toString().trim());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString().trim());
        requestDetailData.setReason(edt_purpose_request_create.getText().toString().trim());


        if (requestDetailData.getRequestName().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập tên đề xuất!");
            hideLoading();
            return;
        }
        // Kiểm tra nếu chưa chọn phương thức
        if (requestDetailData.getRequestMethod() == null) {
            CustomToast.showCustomToast(this, "Vui lòng chọn hình thức!");
            hideLoading();
            return;
        }
        // Log JSON để kiểm tra
//        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//        Log.d("CreateRequestWorkReportActivity", "Data: " + gson.toJson(requestDetailData));

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.businessTripCreateRequestNew(token, requestDetailData);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, apiResponse.getMessage());
                        if (apiResponse.getStatus() == 200) {
                            Intent intent = new Intent(CreateRequestWorkReportActivity.this, HomeActivity.class);
                            intent.putExtra("navigate_to", "newsletter");
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi: " + t.getMessage());
                }
            });
        } else {
            if (listStatus != null && listStatus.getId() == 2) {
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
        btn_confirm.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#e8f7f2")));

        etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = s.toString().trim().isEmpty();
                btn_confirm.setEnabled(!isEmpty);
                btn_confirm.setTextColor(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.secondarycolor : R.color.white));
                btn_confirm.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.white : R.color.secondarycolor)));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_confirm.setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (!reason.isEmpty()) {
                requestDetailData.setRejectReason(reason);
                sendModifyRequest(apiInterface, token, requestDetailData);
                dialog.dismiss();
            } else {
                CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Vui lòng nhập lý do");
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
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String jsonResponse = gson.toJson(requestDetailData);
        Log.d("CreateRequestWorkReportActivity", "Data chỉnh sửa: " + jsonResponse);
        showLoading();
        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        Intent intent = new Intent(CreateRequestWorkReportActivity.this, HomeActivity.class);
                        intent.putExtra("navigate_to", "newsletter");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void sendModifyRequestFollower(ApiInterface apiInterface, String token, RequestDetailData requestDetailData) {
        showLoading();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String jsonResponse = gson.toJson(requestDetailData);
        Log.d("CreateRequestWorkReportActivity", "Data chỉnh sửa: " + jsonResponse);

        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, apiResponse.getMessage());
                } else {
                    CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestWorkReportActivity.this, "Lỗi: " + t.getMessage());
                hideLoading();
            }
        });
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}