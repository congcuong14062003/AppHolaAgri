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
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;
import com.example.appholaagri.model.UploadFileModel.UploadFileResponse;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestBuyNewActivity extends BaseActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc, edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create;
    private TextView title_request, txt_type_request_create;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData; // Biến toàn cục
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;
    private Dialog loadingDialog;
    // over lay
    View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    ConstraintLayout overlayFilterStatus;
    private LinearLayout layout_action_history_request;
    private SwitchCompat switchUrgent;

    private FlexboxLayout fileContainer;
    private List<Uri> selectedFiles = new ArrayList<>();
    private List<RequestDetailData.FileAttachment> uploadedFiles = new ArrayList<>();
    private final int MAX_FILES = 10;
    private static final int PICK_FILES = 200;
    private static final int REQUEST_CODE_FOLLOWER = 100;

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

        // khẩn cấp
        switchUrgent = findViewById(R.id.switch_urgent);
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

        // file
        fileContainer = findViewById(R.id.file_container);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        // Khởi tạo giá trị mặc định
        GroupRequestId = -1;
        GroupRequestType = -1;
        StatusRequest = -1;
        requestId = -1;
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1); // Nhận requestId
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
        }


        renderFiles(); // Hiển thị danh sách file khởi tạo

        // init
        layout_action_history_request.setVisibility(View.GONE);
        txt_status_request_detail.setVisibility(View.GONE);

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
            finish();
        });
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
        // Thiết lập ngày giờ mặc định và vô hiệu hóa chỉnh sửa
        setDefaultDateTime();
        Calendar currentDate = Calendar.getInstance();

        etNgayBatDau.setOnClickListener(null);
        etGioBatDau.setOnClickListener(null);
        etNgayKetThuc.setOnClickListener(null);
        etGioKetThuc.setOnClickListener(null);

        switchUrgent.setChecked(false);
        switchUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requestDetailData.setIsUrgent(isChecked ? 1 : 0);
        });

        // Trong hàm onCreate, sự kiện click cho edt_follower_request_create
        edt_follower_request_create.setOnClickListener(view -> {
            Intent intent1 = new Intent(CreateRequestBuyNewActivity.this, SelectFollowerActivity.class);
            // Truyền danh sách người theo dõi hiện tại
            if (requestDetailData != null && requestDetailData.getFollower() != null) {
                intent1.putExtra("current_followers", new ArrayList<>(requestDetailData.getFollower()));
            } else {
                intent1.putExtra("current_followers", new ArrayList<Follower>());
            }
            startActivityForResult(intent1, REQUEST_CODE_FOLLOWER);
        });
    }







    private void setDefaultDateTime() {
        // Lấy ngày giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Thiết lập giá trị mặc định
        etNgayBatDau.setText(currentDate);
        etGioBatDau.setText(currentTime);
        etNgayKetThuc.setText(currentDate);
        etGioKetThuc.setText(currentTime);

        // Vô hiệu hóa chỉnh sửa
        etNgayBatDau.setEnabled(false);
        etNgayBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

        etGioBatDau.setEnabled(false);
        etGioBatDau.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

        etNgayKetThuc.setEnabled(false);
        etNgayKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

        etGioKetThuc.setEnabled(false);
        etGioKetThuc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
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
                        } else {
                            CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
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

    private void updateUserUI(RequestDetailData requestDetailData) {
        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestBuyNewActivity", "requestDetailData is null");
                return;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String requestData = gson.toJson(requestDetailData);
            Log.d("CreateRequestBuyNewActivity", "Data chi tiết: " + requestData);
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
                        uploadedFiles.add(newAttachment);
                    }
                }
                renderFiles();
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

                    edt_reason_request_create.setEnabled(false);
                    edt_reason_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

//                    edt_follower_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
//                    edt_follower_request_create.setEnabled(false);
//            switchUrgent.setEnabled(false);
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
            adapter = new ActionRequestDetailAdapter(requestDetailData.getApprovalLogs());
            recyclerViewApprovalLogs.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (requestDetailData.getApprovalLogs() != null && requestDetailData.getApprovalLogs().size() > 0) {
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

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
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
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
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

        // Đồng bộ danh sách file trước khi gửi
        syncUploadedFilesWithRequestDetailData();
        Log.d("HandleCreateRequest", "File attachments size: " + (requestDetailData.getFileAttachment() != null ? requestDetailData.getFileAttachment().size() : 0));

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestData = gson.toJson(requestDetailData);
        Log.d("CreateRequestBuyNewActivity", "Data gửi lên: " + requestData);

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.buyNewCreateRequestNew(token, requestDetailData);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                            Intent intent = new Intent(CreateRequestBuyNewActivity.this, HomeActivity.class);
                            intent.putExtra("navigate_to", "newsletter");
                            startActivity(intent);
                            finish();
                        } else {
                            CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
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
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Vui lòng nhập lý do");
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
        // Đồng bộ danh sách file trước khi gửi
        syncUploadedFilesWithRequestDetailData();
        showLoading();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestData = gson.toJson(requestDetailData);
        Log.d("CreateRequestBuyNewActivity", "Data chỉnh sửa: " + requestData);

        Call<ApiResponse<String>> call = apiInterface.modifyRequestBase(token, requestDetailData);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        Intent intent = new Intent(CreateRequestBuyNewActivity.this, HomeActivity.class);
                        intent.putExtra("navigate_to", "newsletter");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void sendModifyRequestFollower(ApiInterface apiInterface, String token, RequestDetailData requestDetailData) {
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
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, apiResponse.getMessage());
                } else {
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
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
                    // Giữ nguyên tên từ uploadedFiles (từ API hoặc đã upload)
                    attachment.setName(existingAttachment.getName());
                    attachment.setPath(existingAttachment.getPath());
                } else {
                    // File mới, lấy tên từ URI
                    attachment.setName(getFileNameFromUri(fileUri));
                    attachment.setPath(filePath);
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
        Log.d("uploadFilesSequentially", "Processing file at index: " + index + ", Total new files: " + newFiles.size());
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
        Log.d("UploadFileDebug", "File Uri: " + fileUri.toString());
        File file = getFileFromUri(this, fileUri);
        if (file == null) {
            Log.e("Upload Failed", "File is null for Uri: " + fileUri);
            uploadFilesSequentially(index + 1, newFiles, onComplete);
            return;
        }
        Log.d("UploadFileDebug", "File Path: " + file.getAbsolutePath());

        String mimeType = getContentResolver().getType(fileUri);
        Log.d("UploadFileDebug", "File MIME Type: " + (mimeType != null ? mimeType : "null"));
        if (mimeType == null || !mimeType.startsWith("image/")) {
            CustomToast.showCustomToast(this, "Vui lòng chọn file ảnh!");
            uploadFilesSequentially(index + 1, newFiles, onComplete);
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Log.d("UploadFileDebug", "MultipartBody.Part - File Name: " + file.getName());
        Log.d("UploadFileDebug", "MultipartBody.Part - Media Type: " + "image/*");

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("CreateRequestBuyNewActivity", "Auth Token: " + token);
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
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                String responseData = gson.toJson(response.body());
                Log.d("CreateRequestBuyNewActivity", "Full Response Data: " + responseData);

                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    UploadFileResponse uploadResponse = response.body().getData();
                    if (uploadResponse.getFinalStatus() == 200) {
                        String fileUrl = "https://haloship.imediatech.com.vn/" + uploadResponse.getFileUrl();
                        RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                        attachment.setName(getFileNameFromUri(fileUri));
                        attachment.setPath(fileUrl);

                        // Thay thế fileUri cũ trong selectedFiles và uploadedFiles
                        int selectedIndex = selectedFiles.indexOf(fileUri);
                        if (selectedIndex != -1) {
                            selectedFiles.set(selectedIndex, Uri.parse(fileUrl));
                        }
                        uploadedFiles.removeIf(uploadFile -> uploadFile.getPath().equals(fileUri.toString()));
                        uploadedFiles.add(attachment);

                        uploadFilesSequentially(index + 1, newFiles, onComplete);
                    } else {
                        Log.e("Upload Failed", "Server returned error: " + uploadResponse.getMessage());
                        hideLoading();
                        CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi tải file: " + uploadResponse.getMessage());
                    }
                } else {
                    Log.e("Upload Failed", "Response not successful: " + response.code() + " - " + response.message());
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi server: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                hideLoading();
                Log.e("Upload Failed", "Failure: " + t.getMessage(), t);
                CustomToast.showCustomToast(CreateRequestBuyNewActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void renderFiles() {
        fileContainer.removeAllViews();
        Log.d("RenderFiles", "selectedFiles size: " + selectedFiles.size() + ", uploadedFiles size: " + uploadedFiles.size());
        for (int i = 0; i < selectedFiles.size(); i++) {
            Uri fileUri = selectedFiles.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_image_preview, fileContainer, false);
            ImageView imageView = itemView.findViewById(R.id.image);
            ImageView btnDelete = itemView.findViewById(R.id.btn_delete);

            if (fileUri.toString().startsWith("http")) {
                Picasso.get()
                        .load(fileUri.toString())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(imageView);
            } else {
                imageView.setImageURI(fileUri);
            }

            final int finalI = i;
            String fileName = uploadedFiles.size() > finalI ? uploadedFiles.get(finalI).getName() : getFileNameFromUri(fileUri);
            imageView.setOnClickListener(v -> showImageDetailDialog(fileUri, fileName));
            if (requestDetailData.getStatus() != null) {
                StatusRequest = requestDetailData.getStatus().getId();
            }
            if (StatusRequest != null && StatusRequest > 1) {
                btnDelete.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(v -> {
                    String filePath = fileUri.toString();
                    selectedFiles.remove(finalI);
                    uploadedFiles.removeIf(attachment -> attachment.getPath().equals(filePath));
                    syncUploadedFilesWithRequestDetailData();
                    Log.d("RenderFiles", "After delete - selectedFiles size: " + selectedFiles.size() + ", uploadedFiles size: " + uploadedFiles.size());
                    renderFiles();
                });
            }
            fileContainer.addView(itemView);
        }

        if (StatusRequest == null || StatusRequest <= 2 && selectedFiles.size() < MAX_FILES) {
            View addView = getLayoutInflater().inflate(R.layout.item_add_image, fileContainer, false);
            addView.setOnClickListener(v -> openGallery());
            fileContainer.addView(addView);
        }
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_FILES);
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        if (fileName == null && uri.toString().startsWith("http")) {
            String path = uri.getPath();
            if (path != null) {
                fileName = path.substring(path.lastIndexOf('/') + 1);
            }
        }
        return fileName != null ? fileName : "unknown_file";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILES && resultCode == RESULT_OK && data != null) {
            List<Uri> newFiles = new ArrayList<>();
            Log.d("FileSelection", "Received data with ClipData: " + (data.getClipData() != null));
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                Log.d("FileSelection", "Number of files selected: " + count);
                for (int i = 0; i < count && selectedFiles.size() + newFiles.size() < MAX_FILES; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    Log.d("FileSelection", "Adding file Uri: " + fileUri);
                    newFiles.add(fileUri);
                    selectedFiles.add(fileUri);
                }
            } else if (data.getData() != null) {
                Log.d("FileSelection", "Adding single file Uri: " + data.getData());
                if (selectedFiles.size() < MAX_FILES) {
                    newFiles.add(data.getData());
                    selectedFiles.add(data.getData());
                }
            }
            uploadFilesSequentially(0, newFiles, null);
        }

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

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}