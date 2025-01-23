package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestActivity extends BaseActivity {
    EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create;
    TextView txt_type_request;
    ImageView backBtnReview, rbMotNgayImage, rbNhieuNgayImage ;
    // Khởi tạo các LinearLayout và ImageView
    LinearLayout rbMotNgayLayout;
    LinearLayout rbNhieuNgayLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request);
        txt_type_request = findViewById(R.id.txt_type_request_create);
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        rbMotNgayLayout = findViewById(R.id.rbMotNgay_create_layout);
        rbNhieuNgayLayout = findViewById(R.id.rbNhieuNgay_create_layout);
        rbMotNgayImage = findViewById(R.id.rbMotNgay_create);
        rbNhieuNgayImage  = findViewById(R.id.rbNhieuNgay_create);

        backBtnReview = findViewById(R.id.backBtnReview_create);
        rbMotNgayImage.setImageResource(R.drawable.checked_radio);
        // Đặt sự kiện click cho "1 ngày"
        rbMotNgayLayout.setOnClickListener(v -> {
            rbMotNgayImage.setImageResource(R.drawable.checked_radio); // Đặt trạng thái "đã chọn"
            rbNhieuNgayImage.setImageResource(R.drawable.unchecked_radio); // Đặt trạng thái "không được chọn"

            // Xử lý thêm logic cho lựa chọn "1 ngày"
            Log.d("Selection", "1 ngày được chọn");
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
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            if(GroupRequestId != null && token != null) {
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }
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
                            RequestDetailData requestDetailData = apiResponse.getData();
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
                            Log.d("SelectedMethod", "User selected: " + selectedMethod.getName());
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
        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage());
        }
    }
}