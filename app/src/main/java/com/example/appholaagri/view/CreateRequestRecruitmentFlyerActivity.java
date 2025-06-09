package com.example.appholaagri.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
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
import com.example.appholaagri.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestRecruitmentFlyerActivity extends AppCompatActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create,
            edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create,
            edt_type_request_create, edt_position_request_create, edt_quantity_request_create, edt_salary_request_create,
            edt_date_request_create, edt_des_request_create, edt_required_request_create;
    private TextView title_request, txt_type_request_create;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData;
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest;
    private RecyclerView recyclerViewApprovalLogs;
    private ActionRequestDetailAdapter adapter;
    private AppCompatButton txt_status_request_detail;
    private CoordinatorLayout create_request_container;
    private LinearLayout layout_action_history_request;
    private Dialog loadingDialog;
    private SwitchCompat switchUrgent;
    private static final int REQUEST_CODE_FOLLOWER = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_recruitment_flyer);


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
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        layout_action_history_request = findViewById(R.id.layout_action_history_request);

        // phần riêng

        edt_type_request_create = findViewById(R.id.edt_type_request_create);
        edt_position_request_create = findViewById(R.id.edt_position_request_create);
        edt_quantity_request_create = findViewById(R.id.edt_quantity_request_create);
        edt_salary_request_create = findViewById(R.id.edt_salary_request_create);
        edt_date_request_create = findViewById(R.id.edt_date_request_create);
        edt_des_request_create = findViewById(R.id.edt_des_request_create);
        edt_required_request_create = findViewById(R.id.edt_required_request_create);




        recyclerViewApprovalLogs = findViewById(R.id.recyclerViewApprovalLogs);
        recyclerViewApprovalLogs.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Intent
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1);
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1);
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
        }

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
            Intent intent1 = new Intent(CreateRequestRecruitmentFlyerActivity.this, SelectFollowerActivity.class);
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
                            CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestRecruitmentFlyerActivity", "Error: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi: " + t.getMessage());
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
                            CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestRecruitmentFlyerActivity", "Error: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void updateUserUI(RequestDetailData requestDetailData) {
        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestRecruitmentFlyerActivity", "requestDetailData is null");
                return;
            }

            // Trạng thái
            if (requestDetailData.getStatus() != null && requestDetailData.getStatus().getName() != null) {
                txt_status_request_detail.setText(requestDetailData.getStatus().getName());
                String colorCode = requestDetailData.getStatus().getColor() != null ? requestDetailData.getStatus().getColor() : "#cccccc";
                int color = Color.parseColor(colorCode);
                txt_status_request_detail.setTextColor(color);
                int backgroundColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
                txt_status_request_detail.setBackgroundTintList(android.content.res.ColorStateList.valueOf(backgroundColor));
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


            // Loại đề xuất
            if (requestDetailData.getRequestMethod() != null) {
                edt_type_request_create.setText(requestDetailData.getRequestMethod().getName());
            }

            // Vị trí cần tuyển
            if (requestDetailData.getRecruitmentReq() != null && requestDetailData.getRecruitmentReq().getPosition() != null) {
                edt_position_request_create.setText(requestDetailData.getRecruitmentReq().getPosition().getName());
            }

            // Số lượng cần tuyển
            if (requestDetailData.getRecruitmentReq() != null) {
                edt_quantity_request_create.setText(String.valueOf(requestDetailData.getRecruitmentReq().getQuantity()));
            }

            // Mức lương
            if (requestDetailData.getRecruitmentReq() != null) {
                edt_salary_request_create.setText(Utils.formatCurrency(String.valueOf(requestDetailData.getRecruitmentReq().getSalary())));
            }

            // Ngày dự định làm việc
            if (requestDetailData.getStartDate() != null) {
                edt_date_request_create.setText(requestDetailData.getStartDate());
            }

            // Mô tả làm việc
            if (requestDetailData.getRecruitmentReq() != null) {
                edt_des_request_create.setText(requestDetailData.getRecruitmentReq().getDescription());
            }

            // Yêu cầu làm việc
            if (requestDetailData.getStartDate() != null) {
                edt_required_request_create.setText(requestDetailData.getRecruitmentReq().getRequirement());
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
                        button.setOnClickListener(v -> handleCreateRequest(requestDetailData, status));
                        actionButtonContainer.addView(button);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CreateRequestRecruitmentFlyerActivity", "Error updating UI: " + e.getMessage());
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

    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus) {
        showLoading();
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());

        // Cập nhật status
        if (listStatus != null) {
            RequestDetailData.Status status = new RequestDetailData.Status(
                    listStatus.getId(),
                    listStatus.getCode(),
                    listStatus.getName(),
                    listStatus.getStatus()
            );
            requestDetailData.setStatus(status);
        }
        // Log JSON để kiểm tra
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Log.d("CreateRequestRecruitmentFlyerActivity", "Data: " + gson.toJson(requestDetailData));

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (requestId == -1) {
            Call<ApiResponse<String>> call = apiInterface.createBaseRequest(token, requestDetailData);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, apiResponse.getMessage());
                        if (apiResponse.getStatus() == 200) {
                            Intent intent = new Intent(CreateRequestRecruitmentFlyerActivity.this, HomeActivity.class);
                            intent.putExtra("navigate_to", "newsletter");
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    hideLoading();
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi: " + t.getMessage());
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
                CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Vui lòng nhập lý do");
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
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, apiResponse.getMessage());
                    if (apiResponse.getStatus() == 200) {
                        Intent intent = new Intent(CreateRequestRecruitmentFlyerActivity.this, HomeActivity.class);
                        intent.putExtra("navigate_to", "newsletter");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideLoading();
                CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi: " + t.getMessage());
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
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, apiResponse.getMessage());
                } else {
                    CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestRecruitmentFlyerActivity.this, "Lỗi: " + t.getMessage());
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