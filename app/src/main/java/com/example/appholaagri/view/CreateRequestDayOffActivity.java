package com.example.appholaagri.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.adapter.CustomSpinnerAdapterCompany;
import com.example.appholaagri.adapter.DiscussionAdapter;
import com.example.appholaagri.adapter.RequestMethodAdapter;
import com.example.appholaagri.config.Config;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.Comments;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.ListStatus;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import com.example.appholaagri.model.UploadFileModel.UploadFileResponse;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestDayOffActivity extends AppCompatActivity {
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
    private LinearLayout layout_action_history_request, comment_container, discussion_layout;
    private SwitchCompat switchUrgent;
    private Spinner spinner_company_request_create;


    // file va comment
    private FlexboxLayout fileContainer;
    private List<Uri> selectedFiles = new ArrayList<>();
    private List<RequestDetailData.FileAttachment> uploadedFiles = new ArrayList<>();
    //    private final int MAX_FILES = 10;
    private static final int PICK_FILES = 200;
    private static final int REQUEST_CODE_FOLLOWER = 100;
    private static final int REQUEST_CODE_CAMERA = 300;


    // Thêm các biến mới cho phần thảo luận
    private ImageView ivUserAvatar, ivAddText, ivAddFile, ivSend;
    private EditText edtDiscussionInput;
    private FlexboxLayout commentFileContainer;
    private List<Uri> selectedCommentFiles = new ArrayList<>(); // Lưu file đính kèm cho comment
    private List<Comments.FileAttachment> uploadedCommentFiles = new ArrayList<>(); // Lưu file đã upload với kiểu đúng
    private static final int PICK_FILES_FOR_COMMENT = 202; // Mã request cho file comment

    private File currentPhotoFile;
    RecyclerView recyclerViewDiscussion;
    DiscussionAdapter discussionAdapter = new DiscussionAdapter();
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_day_off);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        create_request_container = findViewById(R.id.create_request_container);
        backBtnReview = findViewById(R.id.backBtnReview_create);
        title_request = findViewById(R.id.title_request);
        txt_status_request_detail = findViewById(R.id.txt_status_request_detail);
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        switchUrgent = findViewById(R.id.switch_urgent);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        // công ty
        spinner_company_request_create = findViewById(R.id.spinner_company_request_create); // Ánh xạ Spinner
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


        // file
        fileContainer = findViewById(R.id.file_container);
        comment_container = findViewById(R.id.comment_container);
        discussion_layout = findViewById(R.id.discussion_layout);

        // Ánh xạ phần thảo luận
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        edtDiscussionInput = findViewById(R.id.edtDiscussionInput);
        ivAddFile = findViewById(R.id.ivAddFile);
        ivSend = findViewById(R.id.ivSend);
        commentFileContainer = findViewById(R.id.comment_file_container);

        recyclerViewDiscussion = findViewById(R.id.recyclerViewDiscussion);
        recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDiscussion.setAdapter(discussionAdapter);


        // Đăng ký listener cho DiscussionAdapter
        discussionAdapter.setOnFileStatusChangedListener((file, filePosition, commentPosition) -> {
            if (requestDetailData != null && requestDetailData.getComments() != null &&
                    commentPosition < requestDetailData.getComments().size()) {
                Comments originalComment = requestDetailData.getComments().get(commentPosition);
                List<Comments.FileAttachment> originalFiles = originalComment.getFileAttachments();
                if (originalFiles != null && filePosition < originalFiles.size()) {
                    originalFiles.get(filePosition).setStatus(file.getStatus());
                    Log.d("CreateRequestDayOffActivity", "Updated status in requestDetailData for file: " + file.getName() +
                            " at comment position " + commentPosition + ", file position " + filePosition);
                }
            }
        });


        // Lấy thông tin user và avatar (giữ nguyên logic cũ)
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token != null) {
            UserDetailApiHelper.getUserData(this, token, new UserDetailApiHelper.UserDataCallback() {
                @Override
                public void onSuccess(UserData userData) {
                    String userAvatarUrl = userData.getUserAvatar();
                    if (userAvatarUrl != null && !userAvatarUrl.isEmpty()) {
                        Picasso.get().load(userAvatarUrl).placeholder(R.drawable.avatar).into(ivUserAvatar);
                    } else {
                        ivUserAvatar.setImageResource(R.drawable.avatar);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    ivUserAvatar.setImageResource(R.drawable.avatar);
                    Log.e("CreateRequest", "Failed to load user avatar: " + errorMessage);
                }
            });
        }
        edtDiscussionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivSend.setEnabled(s.length() > 0); // Chỉ bật khi có nội dung
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ivSend.setOnClickListener(v -> {
            String content = edtDiscussionInput.getText().toString().trim();
            if (content.isEmpty()) {
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Vui lòng nhập nội dung thảo luận");
                return;
            }
            sendComment(content, uploadedCommentFiles);
            edtDiscussionInput.setText("");
            ivSend.setEnabled(false);
            selectedCommentFiles.clear();
            uploadedCommentFiles.clear();
            renderCommentFiles(); // Cập nhật UI sau khi gửi
        });

        ivAddFile.setOnClickListener(v -> openGalleryForComment());
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1);
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1);
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestDayOffActivity", "StatusRequest: " + StatusRequest);
        }

        // Khởi tạo Spinner cho công ty
        setupCompanySpinner();

        layout_action_history_request.setVisibility(View.GONE);
        txt_status_request_detail.setVisibility(View.GONE);


        if (requestId != -1) {
            create_request_container.setVisibility(View.GONE);
            txt_status_request_detail.setVisibility(View.VISIBLE);
            title_request.setText("Chi tiết đề xuất");
            getDetailRequest(requestId, token);
            discussion_layout.setVisibility(View.VISIBLE);
        } else {
            if (GroupRequestId != null && token != null) {
                getInitFormCreateRequest(token, GroupRequestId);
            }
            discussion_layout.setVisibility(View.GONE);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
                            renderFiles();
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
                hideLoading();
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
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
                            Log.d("CreateRequestDayOffActivity", "requestDetailDataJson: " + "vào");
                            create_request_container.setVisibility(View.VISIBLE);
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                            renderFiles();
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
                hideLoading();
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
            // Làm sạch danh sách trước khi thêm từ API
            selectedFiles.clear();
            uploadedFiles.clear();
            List<RequestDetailData.FileAttachment> fileAttachments = requestDetailData.getFileAttachment();
            if (fileAttachments != null && !fileAttachments.isEmpty()) {
                for (RequestDetailData.FileAttachment attachment : fileAttachments) {
                    if (attachment.getPath() != null && attachment.getName() != null) {
                        Uri fileUri = Uri.parse(attachment.getPath());
                        selectedFiles.add(fileUri);
                        RequestDetailData.FileAttachment newAttachment = new RequestDetailData.FileAttachment();
                        newAttachment.setName(attachment.getName());
                        newAttachment.setPath(attachment.getPath());
                        newAttachment.setStatus(attachment.getStatus() != -1 ? attachment.getStatus() : 1);
                        newAttachment.setId(attachment.getId());
                        uploadedFiles.add(newAttachment);
                        Log.d("UpdateUserUI", "Added file: " + attachment.getName() + ", status: " + attachment.getStatus());
                    }
                }
                renderFiles();
            }

            // Kiểm tra xem listStatus có chứa trạng thái id = 3 hoặc id = 4
            boolean hasApproveStatus = false;
            boolean hasEditStatus = false;
            if (requestDetailData.getListStatus() != null) {
                for (ListStatus status : requestDetailData.getListStatus()) {
                    if (status != null && status.getId() == 4) {
                        hasApproveStatus = true;
                    }
                    if (status != null && status.getId() == 3) {
                        hasEditStatus = true;
                    }
                }
            }

            // Truyền thông tin vào DiscussionAdapter
            discussionAdapter.setHasApproveStatus(hasApproveStatus);

            // Trạng thái
            if (requestDetailData.getStatus() != null) {
                txt_status_request_detail.setText(requestDetailData.getStatus().getName());
                String colorCode = requestDetailData.getStatus().getColor() != null ? requestDetailData.getStatus().getColor() : "#cccccc";
                int color = Color.parseColor(colorCode);
                txt_status_request_detail.setTextColor(color);
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
                txt_status_request_detail.setBackgroundTintList(android.content.res.ColorStateList.valueOf(backgroundColor));
            }

            // Kiểm tra trạng thái chỉnh sửa
            isEdit = hasEditStatus;
            if (!isEdit) {
                comment_container.setVisibility(View.VISIBLE);
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
                spinner_company_request_create.setEnabled(false);
                spinner_company_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            } else {
                comment_container.setVisibility(View.GONE);
            }
            // Comment
            if (requestDetailData != null && requestDetailData.getComments() != null) {
                discussionAdapter.setComments(requestDetailData.getComments());
                discussionAdapter.notifyDataSetChanged();
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
            // Cập nhật danh sách công ty và công ty mặc định
            if (requestDetailData.getCompanyList() != null && !requestDetailData.getCompanyList().isEmpty()) {
                setupCompanySpinner(); // Gọi lại để cập nhật Spinner với dữ liệu mới
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
        syncUploadedFilesWithRequestDetailData();
        syncCommentFilesWithRequestDetailData();
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
        // Kiểm tra nếu chưa chọn phương thức
        if (requestDetailData.getRequestMethod() == null) {
            CustomToast.showCustomToast(this, "Vui lòng chọn hình thức!");
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = s.toString().trim().isEmpty();
                btn_confirm.setEnabled(!isEmpty);
                btn_confirm.setTextColor(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.secondarycolor : R.color.white));
                btn_confirm.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(dialogView.getContext(), isEmpty ? R.color.white : R.color.secondarycolor)));
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
        syncUploadedFilesWithRequestDetailData();
        syncCommentFilesWithRequestDetailData(); // Đảm bảo trạng thái được đồng bộ
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

    private void sendModifyRequestFollower(ApiInterface apiInterface, String token, RequestDetailData requestDetailData) {
        syncUploadedFilesWithRequestDetailData();
        syncCommentFilesWithRequestDetailData(); // Đảm bảo trạng thái được đồng bộ
        showLoading();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String jsonResponse = gson.toJson(requestDetailData);
        Log.d("CreateRequestLateActivity", "Data chỉnh sửa: " + jsonResponse);

        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                } else {
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
                hideLoading();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    // file
    private void syncUploadedFilesWithRequestDetailData() {
        if (requestDetailData != null) {
            List<RequestDetailData.FileAttachment> updatedAttachments = new ArrayList<>();
            for (int i = 0; i < selectedFiles.size(); i++) {
                Uri fileUri = selectedFiles.get(i);
                String filePath = fileUri.toString();
                RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();

                // Tìm attachment trong uploadedFiles theo path
                RequestDetailData.FileAttachment existingAttachment = null;
                for (RequestDetailData.FileAttachment uploadFile : uploadedFiles) {
                    if (uploadFile.getPath().equals(filePath)) {
                        existingAttachment = uploadFile;
                        break;
                    }
                }

                if (existingAttachment != null) {
                    // Giữ nguyên các thuộc tính từ uploadedFiles
                    attachment.setName(existingAttachment.getName());
                    attachment.setId(existingAttachment.getId());
                    attachment.setPath(filePath);
                    attachment.setStatus(existingAttachment.getStatus());
                } else {
                    // File mới, lấy tên từ URI và đặt Status mặc định là 2
                    attachment.setName(getFileNameFromUri(fileUri));
                    attachment.setPath(filePath);
                    attachment.setStatus(2); // Mặc định checked
                }
                updatedAttachments.add(attachment);
            }
            requestDetailData.setFileAttachment(updatedAttachments);
            Log.d("SyncFiles", "Synced selectedFiles size: " + selectedFiles.size() + ", updatedAttachments size: " + updatedAttachments.size());
        } else {
            Log.e("SyncFiles", "requestDetailData is null");
        }
    }

    private void uploadFilesSequentially(int index, List<Uri> newFiles, Runnable onComplete) {
        if (index >= newFiles.size()) {
            hideLoading();
            syncUploadedFilesWithRequestDetailData();
            renderFiles();
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        Uri fileUri = newFiles.get(index);
        File file = getFileFromUri(this, fileUri);
        if (file == null) {
            uploadFilesSequentially(index + 1, newFiles, onComplete);
            return;
        }

        String fileName = getFileNameFromUri(fileUri);
        Log.d("Upload", "Uploading file: " + fileName);
        String mimeType = getContentResolver().getType(fileUri);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String mediaType = "*/*";
        if ("pdf".equals(fileExtension)) mediaType = "application/pdf";
        else if ("doc".equals(fileExtension) || "docx".equals(fileExtension))
            mediaType = "application/msword";
        else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension))
            mediaType = "application/vnd.ms-excel";

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null) {
            hideLoading();
            CustomToast.showCustomToast(this, "Token không tồn tại, vui lòng đăng nhập lại!");
            return;
        }

        showLoading();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<UploadFileResponse>> call = apiInterface.uploadFile(token, body);
        call.enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    UploadFileResponse uploadResponse = response.body().getData();
                    if (uploadResponse.getFinalStatus() == 200) {
                        String fileUrl = Config.BASE_URL_MEDIA + uploadResponse.getFileUrl();
                        RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                        attachment.setName(fileName);
                        attachment.setPath(fileUrl);
                        attachment.setStatus(2);
                        int selectedIndex = selectedFiles.indexOf(fileUri);
                        if (selectedIndex != -1) {
                            selectedFiles.set(selectedIndex, Uri.parse(fileUrl));
                        }
                        uploadedFiles.removeIf(uploadFile -> uploadFile.getPath().equals(fileUri.toString()));
                        uploadedFiles.add(attachment);
                        Log.d("Upload", "Uploaded file: " + fileName + ", URL: " + fileUrl + ", Id: " + attachment.getId());

                        uploadFilesSequentially(index + 1, newFiles, onComplete);
                    } else {
                        hideLoading();
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi tải file: " + uploadResponse.getMessage());
                    }
                } else {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi server: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void renderFiles() {
        fileContainer.removeAllViews();
        for (int i = 0; i < selectedFiles.size(); i++) {
            Uri fileUri = selectedFiles.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_file_preview, fileContainer, false);
            TextView fileNameText = itemView.findViewById(R.id.file_name);
            ImageView btnDelete = itemView.findViewById(R.id.btn_delete);
            ImageView btnDownload = itemView.findViewById(R.id.btn_download);
            ImageView btnCheckFile = itemView.findViewById(R.id.btn_check_file);

            String fileName = uploadedFiles.size() > i ? uploadedFiles.get(i).getName() : getFileNameFromUri(fileUri);
            SpannableString spannableFileName = new SpannableString(fileName);
            spannableFileName.setSpan(new UnderlineSpan(), 0, fileName.length(), 0);
            fileNameText.setText(spannableFileName);

            // Cập nhật icon check/uncheck dựa trên Status
            RequestDetailData.FileAttachment attachment = uploadedFiles.size() > i ? uploadedFiles.get(i) : null;
            if (attachment != null && attachment.getStatus() == 2) {
                btnCheckFile.setImageResource(R.drawable.checked_radio);
            } else {
                btnCheckFile.setImageResource(R.drawable.no_check_radio_create);
            }

            // Xử lý sự kiện click để chuyển đổi trạng thái check/uncheck
            int finalI = i;
            btnCheckFile.setOnClickListener(v -> {
                if (attachment != null) {
                    attachment.setStatus(attachment.getStatus() == 2 ? 1 : 2);
                    btnCheckFile.setImageResource(attachment.getStatus() == 2 ? R.drawable.checked_radio : R.drawable.no_check_radio_create);
                    syncUploadedFilesWithRequestDetailData();
                    Log.d("FileCheck", "File " + fileName + " status changed to: " + attachment.getStatus());
                }
            });

            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if ("pdf".equals(fileExtension) || "doc".equals(fileExtension) || "docx".equals(fileExtension) ||
                    "xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                fileNameText.setOnClickListener(v -> showFileWebView(fileUri, fileName));
            } else {
                fileNameText.setOnClickListener(v -> showImageDetailDialog(fileUri, fileName));
            }

            btnDownload.setOnClickListener(v -> {
                String fileUrl = fileUri.toString();
                downloadFile(fileUrl, fileName);
            });

            // Kiểm tra trạng thái id = 3 và id = 4 trong listStatus
            boolean hasEditStatus = false;
            boolean hasApproveStatus = false;
            if (requestDetailData != null && requestDetailData.getListStatus() != null) {
                for (ListStatus status : requestDetailData.getListStatus()) {
                    if (status != null && status.getId() == 3) {
                        hasEditStatus = true;
                    } else if (status != null && status.getId() == 4) {
                        hasApproveStatus = true;
                    }
                }
            }

            // Điều khiển hiển thị và kích hoạt các nút
            if (requestId == -1 || hasEditStatus) {
                btnDelete.setVisibility(View.VISIBLE);
                btnCheckFile.setVisibility(requestId == -1 ? View.GONE : View.VISIBLE);
                btnDelete.setOnClickListener(v -> {
                    selectedFiles.remove(finalI);
                    uploadedFiles.removeIf(uploadFile -> uploadFile.getPath().equals(fileUri.toString()));
                    syncUploadedFilesWithRequestDetailData();
                    renderFiles();
                });
            } else {
                btnDelete.setVisibility(View.GONE);
                btnCheckFile.setVisibility(View.VISIBLE);
            }
            btnCheckFile.setEnabled(hasApproveStatus);

            fileContainer.addView(itemView);
        }

        // Hiển thị nút thêm file nếu có trạng thái id = 3 hoặc requestId == -1
        boolean hasEditStatus = false;

        if (!Objects.equals(requestDetailData, null) && !Objects.equals(requestDetailData.getListStatus(), null)) {
            for (ListStatus status : requestDetailData.getListStatus()) {
                if (status != null && status.getId() == 3) {
                    hasEditStatus = true;
                    break;
                }
            }
        }
        Log.d("CreateRequestBuyNewActivity", "hasEditStatus" + hasEditStatus);
        if (hasEditStatus || requestId == -1) {
            View addView = getLayoutInflater().inflate(R.layout.item_add_image, fileContainer, false);
            addView.setOnClickListener(v -> openGallery());
            fileContainer.addView(addView);
        }
    }

    // Thêm phương thức mới để hiển thị dialog tùy chọn mở file Excel giống Zalo
    private void showExcelFileOptions(Uri fileUri, String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(fileUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Tạo Intent cho việc chọn ứng dụng
            Intent chooserIntent = Intent.createChooser(intent, "Mở file Excel");
            try {
                startActivity(chooserIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Không tìm thấy ứng dụng hỗ trợ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // Nếu không mở được, chuyển sang tải về
                downloadFile(fileUri.toString(), fileName);
            }
        } else {
            // Nếu không phải file Excel, chuyển sang FileWebViewActivity như trước
            Intent intent = new Intent(this, FileWebViewActivity.class);
            intent.putExtra("fileUrl", fileUri.toString());
            intent.putExtra("fileName", fileName);
            startActivity(intent);
        }
    }

    // Thêm phương thức downloadFile
    private void downloadFile(String fileUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle(fileName)
                .setDescription("Đang tải file...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            dm.enqueue(request);
            Toast.makeText(this, "Đang tải file...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Cập nhật showFileWebView để sử dụng showExcelFileOptions
    private void showFileWebView(Uri fileUri, String fileName) {
        showExcelFileOptions(fileUri, fileName);
    }

    private void showImageDetailDialog(Uri fileUri, String fileName) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image_detail);

        ImageView imageView = dialog.findViewById(R.id.image_detail);
        TextView textFileName = dialog.findViewById(R.id.text_file_name);
        ImageView btnClose = dialog.findViewById(R.id.btn_close_dialog);

        // Hiển thị ảnh
        if (fileUri.toString().startsWith("http")) {
            Picasso.get()
                    .load(fileUri.toString())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(imageView);
        } else {
            imageView.setImageURI(fileUri);
        }

        // Hiển thị tên file
        textFileName.setText(fileName);

        // Đóng dialog khi chạm vào ảnh
//        imageView.setOnClickListener(v -> dialog.dismiss());

        // Đóng dialog khi nhấn nút x_mark
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Ngăn đóng dialog khi chạm ngoài
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    private void openGallery() {
        final CharSequence[] options = {"Ảnh", "Camera", "Tệp", "Drive"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tệp");
        builder.setItems(options, (dialog, which) -> {
            Intent intent = new Intent();
            switch (which) {
                case 0: // Ảnh (bộ nhớ nội bộ)
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    break;
                case 1: // Camera
                    requestCameraPermission(); // Yêu cầu quyền trước
                    return; // Thoát để xử lý quyền trước khi mở camera
                case 2: // Tệp
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    break;
                case 3: // Drive
                    intent.setPackage("com.google.android.apps.docs");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // Chỉ định các MIME type cụ thể cho file Word
                    intent.setType("application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    break;
            }
            startActivityForResult(Intent.createChooser(intent, "Chọn tệp"), PICK_FILES);
        });
        builder.show();
    }

    // Tách logic mở camera thành một phương thức riêng
    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            currentPhotoFile = createImageFile(); // Lưu file tạm
        } catch (IOException ex) {
            Log.e("Camera", "Error creating file: " + ex.getMessage());
            CustomToast.showCustomToast(this, "Lỗi khi tạo file tạm.");
            return;
        }
        if (currentPhotoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.imedia.holaagri.fileprovider",
                    currentPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    // Cập nhật requestCameraPermission để mở camera sau khi được cấp quyền
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Hiển thị lý do tại sao cần quyền
                CustomToast.showCustomToast(this, "Ứng dụng cần quyền camera để chụp ảnh.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                // Yêu cầu quyền nếu chưa được hỏi
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            startCamera();
        }
    }

    // Phương thức tạo file tạm
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme() != null) {
            if (uri.getScheme().equals("content")) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex); // Lấy tên từ DISPLAY_NAME
                    }
                    cursor.close();
                }
                // Nếu không lấy được từ DISPLAY_NAME, thử lấy từ đường dẫn
                if (fileName == null) {
                    fileName = uri.getLastPathSegment();
                }
            } else if (uri.getScheme().equals("file")) {
                fileName = uri.getLastPathSegment();
            } else if (uri.toString().startsWith("http")) {
                String path = uri.getPath();
                if (path != null) {
                    fileName = path.substring(path.lastIndexOf('/') + 1);
                }
            }
        }
        // Nếu vẫn không lấy được tên, sử dụng MIME type để xác định phần mở rộng chính xác
        if (fileName == null || !fileName.contains(".")) {
            String mimeType = getContentResolver().getType(uri);
            if (mimeType != null) {
                if (mimeType.contains("msword")) {
                    fileName = "document_" + System.currentTimeMillis() + ".doc";
                } else if (mimeType.contains("vnd.openxmlformats-officedocument.wordprocessingml")) {
                    fileName = "document_" + System.currentTimeMillis() + ".docx";
                } else if (mimeType.contains("pdf")) {
                    fileName = "document_" + System.currentTimeMillis() + ".pdf";
                } else {
                    fileName = "unknown_file_" + System.currentTimeMillis();
                }
            } else {
                fileName = "unknown_file_" + System.currentTimeMillis();
            }
        }
        // Đảm bảo tên file có phần mở rộng hợp lệ
        if (fileName != null && !fileName.contains(".")) {
            fileName += ".bin"; // Phần mở rộng mặc định nếu không xác định được
        }
        return fileName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) { // Quyền camera cho đề xuất
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                CustomToast.showCustomToast(this, "Quyền camera bị từ chối.");
            }
        } else if (requestCode == 101) { // Quyền camera cho comment
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraForComment();
            } else {
                CustomToast.showCustomToast(this, "Quyền camera bị từ chối.");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILES && resultCode == RESULT_OK && data != null) {
            List<Uri> newFiles = new ArrayList<>();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String mimeType = getContentResolver().getType(fileUri);
                    String fileName = getFileNameFromUri(fileUri);
                    Log.d("FileSelect", "URI: " + fileUri + ", MIME: " + mimeType + ", Tên file: " + fileName);

                    // Thêm file mới với status = 2
                    selectedFiles.add(fileUri);
                    RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                    attachment.setName(fileName);
                    attachment.setPath(fileUri.toString());
                    attachment.setStatus(2); // Mặc định checked
                    uploadedFiles.add(attachment);

                    newFiles.add(fileUri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                String mimeType = getContentResolver().getType(fileUri);
                String fileName = getFileNameFromUri(fileUri);
                Log.d("FileSelect", "URI: " + fileUri + ", MIME: " + mimeType + ", Tên file: " + fileName);

                // Thêm file mới với status = 2
                selectedFiles.add(fileUri);
                RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                attachment.setName(fileName);
                attachment.setPath(fileUri.toString());
                attachment.setStatus(2); // Mặc định checked
                uploadedFiles.add(attachment);

                newFiles.add(fileUri);
            }
            if (!newFiles.isEmpty()) {
                syncUploadedFilesWithRequestDetailData();
                renderFiles();
                uploadFilesSequentially(0, newFiles, null);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (currentPhotoFile != null) {
                Uri imageUri = Uri.fromFile(currentPhotoFile);
                String fileName = currentPhotoFile.getName();
                Log.d("Camera", "Captured file name: " + fileName);

                // Thêm file mới với status = 2
                selectedFiles.add(imageUri);
                RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                attachment.setName(fileName);
                attachment.setPath(imageUri.toString());
                attachment.setStatus(2); // Mặc định checked
                uploadedFiles.add(attachment);

                syncUploadedFilesWithRequestDetailData();
                renderFiles();
                uploadFilesSequentially(0, Collections.singletonList(imageUri), null);
            }
        } else if (requestCode == REQUEST_CODE_FOLLOWER && resultCode == RESULT_OK && data != null) {
            ArrayList<Follower> selectedFollowers = (ArrayList) data.getSerializableExtra("selected_followers");
            boolean callModifyApi = data.getBooleanExtra("call_modify_api", false);
            if (selectedFollowers != null) {
                for (Follower follower : selectedFollowers) {
                    if (follower != null && follower.getName() != null && !follower.getName().startsWith("@")) {
                        follower.setName("@" + follower.getName());
                    }
                }
                requestDetailData.setFollower(selectedFollowers);
                StringBuilder followerNames = new StringBuilder();
                for (Follower follower : selectedFollowers) {
                    followerNames.append(follower.getName()).append(", ");
                }
                String followerText = followerNames.length() > 0 ? followerNames.substring(0, followerNames.length() - 2) : "Không có người theo dõi";
                edt_follower_request_create.setText(followerText);
                if (callModifyApi && requestId != -1) {
                    RequestDetailData.Status status = new RequestDetailData.Status(-1, null, null, 0, null, null);
                    requestDetailData.setStatus(status);
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                    String token = sharedPreferences.getString("auth_token", null);
                    if (token != null) {
                        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
                        sendModifyRequestFollower(apiInterface, token, requestDetailData);
                    } else {
                        CustomToast.showCustomToast(this, "Không tìm thấy token. Vui lòng đăng nhập lại.");
                    }
                }
            } else {
                requestDetailData.setFollower(new ArrayList<>());
                edt_follower_request_create.setText("Không có người theo dõi");
            }
        } else if (requestCode == PICK_FILES_FOR_COMMENT && resultCode == RESULT_OK) {
            List<Uri> newFiles = new ArrayList<>();
            if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    newFiles.add(fileUri);
                    selectedCommentFiles.add(fileUri);
                }
            } else if (data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                newFiles.add(fileUri);
                selectedCommentFiles.add(fileUri);
            } else if (currentPhotoFile != null) { // Xử lý ảnh từ camera
                Uri imageUri = Uri.fromFile(currentPhotoFile);
                String fileName = currentPhotoFile.getName();
                newFiles.add(imageUri);
                selectedCommentFiles.add(imageUri);
                Log.d("CameraComment", "Captured file name: " + fileName);
            }
            if (!newFiles.isEmpty()) {
                uploadCommentFilesSequentially(0, newFiles, () -> renderCommentFiles());
            }
        }
    }

    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(context.getCacheDir(), fileName);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                file = tempFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    // comment
    private void sendComment(String content, List<Comments.FileAttachment> files) {
        showLoading();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null || requestId == -1) {
            hideLoading();
            CustomToast.showCustomToast(this, "Token hoặc requestId không hợp lệ.");
            return;
        }


        Comments comment = new Comments(content, new ArrayList<>(files), 0, requestId);
//        comment.setComment(content);
//
//        if (!files.isEmpty()) {
//            comment.setFileAttachments(new ArrayList<>(files));
//        }
//        comment.setId(0);
//        comment.setRequestId(requestId);

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestData = gson.toJson(comment);
        Log.d("CreateRequestDayOffActivity", "Datacomment: " + requestData);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<String>> call = apiInterface.createComment(token, comment);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, apiResponse.getMessage());
                        getDetailRequest(requestId, token); // Cập nhật lại danh sách comment
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
    }

    private void openGalleryForComment() {
        final CharSequence[] options = {"Ảnh", "Camera", "Tệp", "Drive"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tệp");
        builder.setItems(options, (dialog, which) -> {
            Intent intent = new Intent();
            switch (which) {
                case 0: // Ảnh (bộ nhớ nội bộ)
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    break;
                case 1: // Camera
                    requestCameraPermissionForComment(); // Yêu cầu quyền trước
                    return; // Thoát để xử lý quyền trước khi mở camera
                case 2: // Tệp
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    break;
                case 3: // Drive
                    intent.setPackage("com.google.android.apps.docs");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/pdf|application/vnd.ms-excel|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    break;
            }
            startActivityForResult(Intent.createChooser(intent, "Chọn tệp"), PICK_FILES_FOR_COMMENT);
        });
        builder.show();
    }

    private void requestCameraPermissionForComment() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                CustomToast.showCustomToast(this, "Ứng dụng cần quyền camera để chụp ảnh.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101); // Sử dụng mã khác để phân biệt
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } else {
            startCameraForComment();
        }
    }

    private void startCameraForComment() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            currentPhotoFile = createImageFile(); // Sử dụng cùng file tạm
        } catch (IOException ex) {
            Log.e("Camera", "Error creating file: " + ex.getMessage());
            CustomToast.showCustomToast(this, "Lỗi khi tạo file tạm.");
            return;
        }
        if (currentPhotoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.imedia.holaagri.fileprovider",
                    currentPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, PICK_FILES_FOR_COMMENT); // Sử dụng cùng request code với comment
        }
    }

    private void renderCommentFiles() {
        commentFileContainer.removeAllViews();
        for (int i = 0; i < selectedCommentFiles.size(); i++) {
            Uri fileUri = selectedCommentFiles.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_file_comment_preview, commentFileContainer, false);
            TextView fileNameText = itemView.findViewById(R.id.file_name);
            ImageView btnDelete = itemView.findViewById(R.id.btn_delete);
            ImageView btnDownload = itemView.findViewById(R.id.btn_download);

            String fileName = getFileNameFromUri(fileUri);
            SpannableString spannableFileName = new SpannableString(fileName);
            spannableFileName.setSpan(new UnderlineSpan(), 0, fileName.length(), 0);
            fileNameText.setText(spannableFileName);

            btnDelete.setVisibility(View.VISIBLE);
            int finalI = i;
            btnDelete.setOnClickListener(v -> {
                selectedCommentFiles.remove(finalI);
                uploadedCommentFiles.removeIf(attachment -> attachment.getPath().equals(fileUri.toString()));
                renderCommentFiles();
            });

            // Handle download button click
            btnDownload.setOnClickListener(v -> {
                String fileUrl = fileUri.toString();
                downloadFile(fileUrl, fileName);
            });

            // Xử lý click vào tên file để mở file hoặc hiển thị tùy chọn
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if ("pdf".equals(fileExtension) || "doc".equals(fileExtension) || "docx".equals(fileExtension) ||
                    "xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                fileNameText.setOnClickListener(v -> showFileWebView(fileUri, fileName));
            } else {
                fileNameText.setOnClickListener(v -> showImageDetailDialog(fileUri, fileName));
            }

            commentFileContainer.addView(itemView);
        }
    }


    private void uploadCommentFilesSequentially(int index, List<Uri> newFiles, Runnable onComplete) {
        if (index >= newFiles.size()) {
            hideLoading();
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        Uri fileUri = newFiles.get(index);
        File file = getFileFromUri(this, fileUri);
        if (file == null) {
            uploadCommentFilesSequentially(index + 1, newFiles, onComplete);
            return;
        }

        String fileName = getFileNameFromUri(fileUri);
        Log.d("UploadComment", "Uploading file: " + fileName);
        String mimeType = getContentResolver().getType(fileUri);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String mediaType = "*/*";
        if ("pdf".equals(fileExtension)) mediaType = "application/pdf";
        else if ("doc".equals(fileExtension) || "docx".equals(fileExtension))
            mediaType = "application/msword";
        else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension))
            mediaType = "application/vnd.ms-excel";

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null) {
            hideLoading();
            CustomToast.showCustomToast(this, "Token không tồn tại, vui lòng đăng nhập lại!");
            return;
        }

        showLoading();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<UploadFileResponse>> call = apiInterface.uploadFile(token, body);
        call.enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    UploadFileResponse uploadResponse = response.body().getData();
                    if (uploadResponse.getFinalStatus() == 200) {
                        String fileUrl = Config.BASE_URL_MEDIA + uploadResponse.getFileUrl();
                        Comments.FileAttachment attachment = new Comments.FileAttachment();
                        attachment.setId(uploadedCommentFiles.size() + 1); // Tạm thời gán ID
                        attachment.setName(fileName);
                        attachment.setPath(fileUrl);
                        attachment.setStatus(1); // Theo cấu trúc request, status = 2
                        uploadedCommentFiles.add(attachment);

                        // Cập nhật selectedCommentFiles với fileUrl tại vị trí index
                        if (index < selectedCommentFiles.size()) {
                            selectedCommentFiles.set(index, Uri.parse(fileUrl));
                        }

                        uploadCommentFilesSequentially(index + 1, newFiles, onComplete);
                    } else {
                        hideLoading();
                        CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi tải file: " + uploadResponse.getMessage());
                    }
                } else {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi server: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestDayOffActivity.this, "Lỗi: " + t.getMessage());
            }
        });

    }

    // Phương thức mới để đồng bộ trạng thái file trong comments (giữ nguyên để kiểm tra)
    private void syncCommentFilesWithRequestDetailData() {
        if (requestDetailData != null && requestDetailData.getComments() != null) {
            for (int i = 0; i < requestDetailData.getComments().size(); i++) {
                Comments comment = requestDetailData.getComments().get(i);
                List<Comments.FileAttachment> fileAttachments = comment.getFileAttachments();
                if (fileAttachments != null) {
                    for (int j = 0; j < fileAttachments.size(); j++) {
                        Log.d("SyncCommentFiles", "Synced status for file at comment " + i + ", file " + j + ": " + fileAttachments.get(j).getStatus());
                    }
                }
            }
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