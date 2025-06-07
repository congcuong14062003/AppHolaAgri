package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.FollowerAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.ListUserModel.ListUserRequest;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFollowerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FollowerAdapter adapter;
    private List<Follower> allFollowers = new ArrayList<>();
    private List<Follower> selectedFollowers = new ArrayList<>();
    private EditText edtSearch;
    private TextView tvSelectedCount;
    private Button btnConfirm;
    private String currentSearchQuery = "";
    private ImageView backBtnReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_follower);

        // Ánh xạ
        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edtSearch);
        backBtnReview = findViewById(R.id.backBtnReview);
        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        btnConfirm = findViewById(R.id.btnConfirm);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nhận danh sách người theo dõi hiện tại
        Intent intent = getIntent();
        if (intent.hasExtra("current_followers")) {
            selectedFollowers = (ArrayList<Follower>) intent.getSerializableExtra("current_followers");
            if (selectedFollowers == null) {
                selectedFollowers = new ArrayList<>();
            }
        }

        // Cập nhật số lượng người được chọn
        updateSelectedCount();

        // Lấy danh sách người dùng từ API
        fetchFollowers(currentSearchQuery);

        // Sự kiện nút back
        backBtnReview.setOnClickListener(v -> finish());

        // Sự kiện nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_followers", new ArrayList<>(selectedFollowers));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Sự kiện tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                fetchFollowers(currentSearchQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchFollowers(String keySearch) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null) {
            CustomToast.showCustomToast(this, "Không tìm thấy token. Vui lòng đăng nhập lại.");
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        ListUserRequest request = new ListUserRequest(Arrays.asList(-1), keySearch);
        Call<ApiResponse<List<Follower>>> call = apiInterface.listUser(token, request);

        call.enqueue(new Callback<ApiResponse<List<Follower>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Follower>>> call, Response<ApiResponse<List<Follower>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Follower>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        allFollowers = apiResponse.getData();
                        if (allFollowers == null) {
                            allFollowers = new ArrayList<>();
                        }

                        // Khởi tạo hoặc cập nhật adapter
                        if (adapter == null) {
                            adapter = new FollowerAdapter(allFollowers, selectedFollowers, follower -> {
                                // Xử lý chọn/bỏ chọn
                                if (selectedFollowers.stream().anyMatch(selected -> selected.getId() == follower.getId())) {
                                    selectedFollowers.removeIf(selected -> selected.getId() == follower.getId());
                                } else {
                                    selectedFollowers.add(follower);
                                }
                                updateSelectedCount();
                                adapter.notifyDataSetChanged();
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateList(allFollowers);
                        }
                    } else {
                        CustomToast.showCustomToast(SelectFollowerActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(SelectFollowerActivity.this, "Lỗi kết nối.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Follower>>> call, Throwable t) {
                Log.e("SelectFollowerActivity", "API call failed: " + t.getMessage());
                CustomToast.showCustomToast(SelectFollowerActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void updateSelectedCount() {
        tvSelectedCount.setText(String.valueOf(selectedFollowers.size()));
    }
}