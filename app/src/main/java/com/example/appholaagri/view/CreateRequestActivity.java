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
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestActivity extends AppCompatActivity {
    EditText txt_name_request, txt_name_employye_request_create, txt_part_request_create, txt_reason_request, txt_manager_direct_request,txt_fixed_reviewer_request,txt_follower_request;
    AppCompatButton txt_status_request;
    LinearLayout reason_refused_request_layout;
    EditText etNgayBatDau, etGioBatDau, etNgayKetThuc, etGioKetThuc;
    TextView txt_type_request;
    ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request);
        txt_type_request = findViewById(R.id.txt_type_request_create);
        txt_name_employye_request_create = findViewById(R.id.txt_name_employye_request_create);
//        txt_name_employye_request = findViewById(R.id.txt_name_employye_request_create);
//        txt_part_request_create = findViewById(R.id.txt_part_request_create);
//        etNgayBatDau = findViewById(R.id.etNgayBatDau);
//        etGioBatDau = findViewById(R.id.etGioBatDau);
//        etNgayKetThuc = findViewById(R.id.etNgayKetThuc);
//        etGioKetThuc = findViewById(R.id.etGioKetThuc);
//        txt_reason_request = findViewById(R.id.txt_reason_request_create);
//        txt_manager_direct_request = findViewById(R.id.txt_manager_direct_request_create);
//        txt_fixed_reviewer_request = findViewById(R.id.txt_fixed_reviewer_request_create);
//        txt_follower_request = findViewById(R.id.txt_follower_request_create);
        backBtnReview = findViewById(R.id.backBtnReview_create);
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
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
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
            // Chuyển đối tượng thành JSON bằng Gson
//            Gson gson = new Gson();
//            String json = gson.toJson(requestDetailData.getDepartment().getName());
//            Log.d("RequestDetailData_JSON", json);

            txt_type_request.setText(requestDetailData.getRequestGroup().getName());
            txt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());



        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage());
        }
    }
}