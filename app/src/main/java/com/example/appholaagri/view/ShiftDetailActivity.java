package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListWorkShiftDetailAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse;
import com.example.appholaagri.model.ListWorkShiftModel.WorkShiftListWrapper;
import com.example.appholaagri.model.ShiftDetailModel.ShiftDetailRequest;
import com.example.appholaagri.model.ShiftDetailModel.ShiftDetailResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftDetailActivity extends AppCompatActivity {
    private ListWorkShiftDetailAdapter adapter;

    private ShapeableImageView avatarWorkShift;
    private TextView nameWorkShift, departmentWorkShift;
    private RecyclerView listDetailShift;
    private ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shift_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        avatarWorkShift = findViewById(R.id.avatar_work_shift);
        nameWorkShift = findViewById(R.id.name_work_shift);
        departmentWorkShift = findViewById(R.id.department_work_shift);
        listDetailShift = findViewById(R.id.list_detail_shift);
        backBtnReview = findViewById(R.id.backBtnReview);


        backBtnReview.setOnClickListener(view -> {
            finish();
        });
        // Lấy WorkShiftData từ Intent
        ListWorkShiftResponse.WorkShiftData workShiftData = (ListWorkShiftResponse.WorkShiftData) getIntent().getSerializableExtra("workShiftData");

        if (workShiftData != null) {
            // Tạo ShiftDetailRequest từ WorkShiftData
            ShiftDetailRequest request = new ShiftDetailRequest(
                    workShiftData.getEmployee(),
                    workShiftData.getTeam(),
                    workShiftData.getWorkShiftListDetail(),
                    workShiftData.getType(),
                    workShiftData.getEmployeeId()
            );
            // (Tùy chọn) Nếu cần gọi API để lấy thêm dữ liệu, lấy token từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            String token = sharedPreferences.getString("auth_token", null);
            if (token != null) {
                // Gọi API nếu cần (bỏ comment và hoàn thiện hàm getDetailShift nếu cần)

                getDetailShift(token, request);
            } else {
                Toast.makeText(this, "Không tìm thấy token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có dữ liệu nhân viên", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getDetailShift(String token, ShiftDetailRequest shiftDetailRequest) {

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestDetailDataJson = gson.toJson(shiftDetailRequest);
        Log.d("ShiftDetailActivity", "dataaaaaaa request: " + requestDetailDataJson);


        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<WorkShiftListWrapper>> call = apiInterface.detailWokShift(token, shiftDetailRequest);
        call.enqueue(new Callback<ApiResponse<WorkShiftListWrapper>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<WorkShiftListWrapper>> call, Response<ApiResponse<WorkShiftListWrapper>> response) {
                Log.d("ShiftDetailActivity", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<WorkShiftListWrapper> apiResponse = response.body();

                    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                    String requestDetailDataJson = gson.toJson(response.body());
                    Log.d("ShiftDetailActivity", "dataaaaaaa: " + requestDetailDataJson);
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        ListWorkShiftResponse.WorkShiftData detailData = apiResponse.getData().getData().get(0);
                        updateUiDetail(detailData);
                    } else {
                        String errorMessage = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Không có dữ liệu chi tiết";
                        Toast.makeText(ShiftDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("ShiftDetailActivity", errorMessage);
                    }
                } else {
                    String errorMessage = "API response unsuccessful";
                    Toast.makeText(ShiftDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("ShiftDetailActivity", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WorkShiftListWrapper>> call, Throwable t) {
                String errorMessage = t.getMessage() != null ? t.getMessage() : "Network error";
                Toast.makeText(ShiftDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ShiftDetailActivity", "Error: " + errorMessage);
            }
        });
    }
    private void updateUiDetail(ListWorkShiftResponse.WorkShiftData detailData) {
        // Cập nhật avatar
        if (detailData.getEmployee().getUrl() != null && !detailData.getEmployee().getUrl().isEmpty()) {
            Picasso.get()
                    .load(detailData.getEmployee().getUrl())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(avatarWorkShift);
        }

        // Cập nhật tên và phòng ban
        nameWorkShift.setText(detailData.getEmployee().getCode() + " - " + detailData.getEmployee().getName());
        departmentWorkShift.setText(detailData.getTeam().getName());

        // Cập nhật danh sách chi tiết ca làm
        if (detailData.getWorkShiftListDetail() != null) {
            if (adapter == null) {
                adapter = new ListWorkShiftDetailAdapter(detailData.getWorkShiftListDetail());
                listDetailShift.setLayoutManager(new LinearLayoutManager(this));
                listDetailShift.setAdapter(adapter);
            } else {
                adapter.updateData(detailData.getWorkShiftListDetail());
            }
        }
    }

}