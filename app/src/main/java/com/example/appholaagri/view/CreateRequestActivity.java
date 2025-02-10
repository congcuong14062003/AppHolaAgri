package com.example.appholaagri.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ForgotPasswordModel.ForgotPasswordRequest;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestActivity extends BaseActivity {
    EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create,
            edt_manager_direct_request_create,edt_fixed_reviewer_request_create, edt_follower_request_create,
            edt_reason_request_create;
    TextView txt_type_request;
    ImageView backBtnReview, rbMotNgayImage, rbNhieuNgayImage ;
    // Khởi tạo các LinearLayout và ImageView
    LinearLayout rbMotNgayLayout,cancel_create_request_layout, form_time_layout;
    LinearLayout rbNhieuNgayLayout;

    EditText etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc;
    private RequestDetailData requestDetailData; // Biến toàn cục
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request);
        txt_type_request = findViewById(R.id.txt_type_request_create);
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        rbMotNgayLayout = findViewById(R.id.rbMotNgay_create_layout);
        rbNhieuNgayLayout = findViewById(R.id.rbNhieuNgay_create_layout);
        rbMotNgayImage = findViewById(R.id.rbMotNgay_create);
        rbNhieuNgayImage  = findViewById(R.id.rbNhieuNgay_create);
        edt_manager_direct_request_create  = findViewById(R.id.edt_manager_direct_request_create);
        edt_fixed_reviewer_request_create  = findViewById(R.id.edt_fixed_reviewer_request_create);
        edt_follower_request_create  = findViewById(R.id.edt_follower_request_create);
        form_time_layout = findViewById(R.id.form_time_layout);
        backBtnReview = findViewById(R.id.backBtnReview_create);
        cancel_create_request_layout = findViewById(R.id.cancel_create_request_layout);
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);


        rbMotNgayImage.setImageResource(R.drawable.checked_radio);

        etNgayBatDau = findViewById(R.id.etNgayBatDau);
        etGioBatDau = findViewById(R.id.etGioBatDau);
        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
        etGioKetThuc = findViewById(R.id.etGioKetThuc);

        // Chọn ngày & giờ bắt đầu
        etNgayBatDau.setOnClickListener(v -> showDatePicker(etNgayBatDau));
        etGioBatDau.setOnClickListener(v -> showTimePicker(etGioBatDau));

        // Chọn ngày & giờ kết thúc
        etNgayKetThuc.setOnClickListener(v -> showDatePicker(etNgayKetThuc));
        etGioKetThuc.setOnClickListener(v -> showTimePicker(etGioKetThuc));
        // Đặt sự kiện click cho "1 ngày"
        rbMotNgayLayout.setOnClickListener(v -> {
            rbMotNgayImage.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbNhieuNgayImage.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"

            // Xử lý thêm logic cho lựa chọn "1 ngày"
            Log.d("Selection", "1 ngày được chọn");
        });
        cancel_create_request_layout.setOnClickListener(view -> {
            onBackPressed();
        });

        // Đặt sự kiện click cho "Nhiều ngày"
        rbNhieuNgayLayout.setOnClickListener(v -> {
            rbNhieuNgayImage.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbMotNgayImage.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"

            // Xử lý thêm logic cho lựa chọn "Nhiều ngày"
            Log.d("Selection", "Nhiều ngày được chọn");
        });

        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        Intent intent = getIntent();
        if (intent != null) {
            Integer GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            // điều kiện để check theo group request
            if(GroupRequestId == 2) {
                form_time_layout.setVisibility(View.GONE);
            }
            Log.d("CreateRequestActivity", "GroupRequestId" + GroupRequestId);
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            if(GroupRequestId != null && token != null) {
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }
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
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Hàm hiển thị TimePickerDialog
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
    private void getInitFormCreateRequest(String token,  int GroupRequestId) {
        Log.d("CreateRequestActivity", "GroupRequestId: " + GroupRequestId);
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
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.");
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
            // Hiển thị thông tin cơ bản
            txt_type_request.setText(requestDetailData.getRequestGroup().getName());
            edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            if(requestDetailData.getDirectManager() != null) {
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
                // Tìm Spinner
                Spinner spinnerHinhThuc = findViewById(R.id.spinnerHinhThuc);

                // Chuyển danh sách `RequestMethod` thành danh sách chuỗi tên hiển thị
                List<String> methodNames = new ArrayList<>();
                methodNames.add("--Vui lòng chọn--"); // Thêm giá trị mặc định vào đầu danh sách
                for (RequestMethod method : listMethods) {
                    methodNames.add(method.getName()); // Giả sử RequestMethod có phương thức `getName()`
                }

                // Tạo ArrayAdapter cho Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        methodNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerHinhThuc.setAdapter(adapter);

                // Lắng nghe sự kiện chọn item
                spinnerHinhThuc.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            // Người dùng chưa chọn gì, vị trí mặc định
                            Log.d("SelectedMethod", "No method selected (default item)");
                        } else {
                            RequestMethod selectedMethod = listMethods.get(position - 1); // Trừ 1 vì có mục mặc định
                            requestDetailData.setRequestMethod(selectedMethod);
                            Gson gson = new GsonBuilder()
                                    .setPrettyPrinting() // Định dạng đẹp
                                    .serializeNulls()    // Giữ lại giá trị null
                                    .create();

                            String jsonLog = gson.toJson(selectedMethod); // Chuyển object thành JSON
                            Log.d("SelectedMethod", "User selected: " + jsonLog);
                            // Bạn có thể xử lý thêm với `selectedMethod` tại đây
                        }
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                        Log.d("SelectedMethod", "No method selected");
                    }
                });
            } else {
                Log.e("SpinnerData", "ListMethod is null or empty");
            }


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

    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus1 ) {
        requestDetailData.setRequestName(edt_name_request_create.getText().toString());
        requestDetailData.setEndDate(etNgayKetThuc.getText().toString());
        requestDetailData.setEndTime(etGioKetThuc.getText().toString());
        requestDetailData.setStartDate(etNgayBatDau.getText().toString());
        requestDetailData.setStartTime(etGioBatDau.getText().toString());
        // Cập nhật lý do (Reason)
        requestDetailData.setReason(edt_reason_request_create.getText().toString());

        // Cập nhật hình thức (Request Method)


        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // Định dạng đẹp
                .serializeNulls()    // Giữ lại giá trị null
                .create();

        String jsonLog = gson.toJson(requestDetailData.getRequestMethod()); // Chuyển object thành JSON

        Log.d("CreateRequestActivity", "listStatus selected: " + jsonLog);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Tạo object để gửi request
        GroupRequestCreateRequest groupRequestCreateRequest = new GroupRequestCreateRequest();

        // Gán dữ liệu từ API init form (giả sử đã lưu trong `requestDetailData`)
        groupRequestCreateRequest.setContact(requestDetailData.getContact());
        groupRequestCreateRequest.setDateType(requestDetailData.getDateType());


        groupRequestCreateRequest.setDuration(requestDetailData.getDuration());
        groupRequestCreateRequest.setEndDate(requestDetailData.getEndDate());
        groupRequestCreateRequest.setEndTime(requestDetailData.getEndTime());


        groupRequestCreateRequest.setReason(edt_reason_request_create.getText().toString());
        groupRequestCreateRequest.setRejectReason("");


        // **Gán dữ liệu cho requestGroup**
        GroupRequestCreateRequest.RequestGroup requestGroup = new GroupRequestCreateRequest.RequestGroup(
                requestDetailData.getRequestGroup().getCode(),  // code
                requestDetailData.getRequestGroup().getId(),    // id
                requestDetailData.getRequestGroup().getName(),  // name
                requestDetailData.getRequestGroup().getStatus() // status
        );
        groupRequestCreateRequest.setRequestGroup(requestGroup);

        GroupRequestCreateRequest.RequestMethod requestMethod = new GroupRequestCreateRequest.RequestMethod(
                requestDetailData.getRequestMethod().getCode(),  // code
                requestDetailData.getRequestMethod().getId(),    // id
                requestDetailData.getRequestMethod().getName(),  // name
                requestDetailData.getRequestMethod().getStatus() // status
        );
        groupRequestCreateRequest.setRequestMethod(requestMethod);


        GroupRequestCreateRequest.Status status = new GroupRequestCreateRequest.Status(
                listStatus1.getCode(),  // code
                listStatus1.getId(),    // id
                listStatus1.getName(),  // name
                listStatus1.getStatus() // status
        );
        groupRequestCreateRequest.setStatus(status);


        groupRequestCreateRequest.setRequestId(requestDetailData.getRequestId());

        groupRequestCreateRequest.setRequestName(requestDetailData.getRequestName());
        groupRequestCreateRequest.setStartDate(requestDetailData.getStartDate());
        groupRequestCreateRequest.setStartTime(requestDetailData.getStartTime());
        groupRequestCreateRequest.setType(requestDetailData.getType());

        String dataObject = gson.toJson(groupRequestCreateRequest); // Chuyển object thành JSON

        Log.d("CreateRequestActivity", "data to createeeeee: " + dataObject);



        // Gửi request tạo mới
        Call<ApiResponse<String>> call = apiInterface.dayOffCreateRequest(token, groupRequestCreateRequest);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                Log.d("CreateRequest", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        CustomToast.showCustomToast(CreateRequestActivity.this, apiResponse.getMessage());
                        startActivity(new Intent(CreateRequestActivity.this, RequestActivity.class));
                    } else {
                        CustomToast.showCustomToast(CreateRequestActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(CreateRequestActivity.this, "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                CustomToast.showCustomToast(CreateRequestActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }
}