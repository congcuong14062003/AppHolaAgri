package com.example.appholaagri.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.example.appholaagri.adapter.DepartmentAdapter;
import com.example.appholaagri.adapter.FollowerAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.DepartmentModel.Department;
import com.example.appholaagri.model.ListUserModel.ListUserRequest;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class SelectFollowerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FollowerAdapter adapter;
    private List<Follower> allFollowers = new ArrayList<>();
    private List<Follower> selectedFollowers = new ArrayList<>();
    private EditText edtSearch;
    private TextView tvSelectedCount;
    private Button btnConfirm;
    private String currentSearchQuery = "";
    private ImageView backBtnReview, filterIcon;
    private List<Integer> selectedDepartmentIds = new ArrayList<>(Arrays.asList(-1)); // Default to "All"
    private DepartmentAdapter departmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_follower);

        // Ánh xạ
        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edtSearch);
        backBtnReview = findViewById(R.id.backBtnReview);
        filterIcon = findViewById(R.id.filterIcon);
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

        // Sự kiện nút filter
        filterIcon.setOnClickListener(v -> showDepartmentPopup());

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                fetchFollowers(currentSearchQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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
        ListUserRequest request = new ListUserRequest(selectedDepartmentIds, keySearch);
        Call<ApiResponse<List<Follower>>> call = apiInterface.listUser(token, request);

        call.enqueue(new retrofit2.Callback<ApiResponse<List<Follower>>>() {
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

    private void showDepartmentPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.department_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Thiết lập kích thước popup: 90% chiều rộng, 50% chiều cao màn hình
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.9);
        int height = (int) (displayMetrics.heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);

        // Ánh xạ
        RecyclerView rvDepartments = dialog.findViewById(R.id.rv_departments);
        Button btnCancel = dialog.findViewById(R.id.btn_popup_cancel);
        Button btnConfirmDepartments = dialog.findViewById(R.id.btn_popup_confirm);
        rvDepartments.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách tạm thời để theo dõi các lựa chọn
        List<Integer> tempSelectedDepartmentIds = new ArrayList<>(selectedDepartmentIds);

        // Lấy danh sách phòng ban từ API
        fetchDepartments(departmentList -> {
            // Kiểm tra xem đã có mục "Tất cả" trong danh sách chưa
            boolean hasAllDepartment = departmentList.stream().anyMatch(dept -> dept.getId() == -1 && "Tất cả".equals(dept.getName()));
            if (!hasAllDepartment) {
                // Thêm mục "Tất cả" vào đầu danh sách nếu chưa có
                Department allDepartment = new Department();
                allDepartment.setId(-1);
                allDepartment.setName("Tất cả");
                departmentList.add(0, allDepartment);
            }

            departmentAdapter = new DepartmentAdapter(departmentList, tempSelectedDepartmentIds, department -> {
                int departmentId = department.getId();
                if (departmentId == -1) {
                    // Chọn "Tất cả"
                    tempSelectedDepartmentIds.clear();
                    tempSelectedDepartmentIds.add(-1);
                } else {
                    // Xử lý chọn phòng ban cụ thể
                    if (tempSelectedDepartmentIds.contains(-1)) {
                        tempSelectedDepartmentIds.remove((Integer) (-1));
                    }
                    if (tempSelectedDepartmentIds.contains(departmentId)) {
                        tempSelectedDepartmentIds.remove((Integer) departmentId);
                    } else {
                        tempSelectedDepartmentIds.add(departmentId);
                    }
                    // Nếu không có phòng ban nào được chọn, mặc định chọn "Tất cả"
                    if (tempSelectedDepartmentIds.isEmpty()) {
                        tempSelectedDepartmentIds.add(-1);
                    }
                }
                departmentAdapter.updateList(departmentList, tempSelectedDepartmentIds);
            });
            rvDepartments.setAdapter(departmentAdapter);
        });

        // Sự kiện nút xác nhận: Gửi danh sách ID phòng ban tới API
        btnConfirmDepartments.setOnClickListener(v -> {
            selectedDepartmentIds.clear();
            selectedDepartmentIds.addAll(tempSelectedDepartmentIds);
            fetchFollowers(currentSearchQuery); // Gọi API với danh sách ID phòng ban đã chọn
            dialog.dismiss();
        });

        // Sự kiện nút hủy: Không thay đổi danh sách ID phòng ban
        btnCancel.setOnClickListener(v -> {
            // Không cập nhật selectedDepartmentIds, giữ nguyên trạng thái trước đó
            dialog.dismiss();
        });

        // Thêm animation
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private void fetchDepartments(ResultCallback<List<Department>> callback) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null) {
            CustomToast.showCustomToast(this, "Không tìm thấy token. Vui lòng đăng nhập lại.");
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<List<Department>>> call = apiInterface.listDepartment(token);

        call.enqueue(new retrofit2.Callback<ApiResponse<List<Department>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Department>>> call, Response<ApiResponse<List<Department>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Department>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        List<Department> departmentList = apiResponse.getData();
                        if (departmentList == null) {
                            departmentList = new ArrayList<>();
                        }
                        callback.onResult(departmentList);
                    } else {
                        CustomToast.showCustomToast(SelectFollowerActivity.this, apiResponse.getMessage());
                    }
                } else {
                    CustomToast.showCustomToast(SelectFollowerActivity.this, "Lỗi kết nối.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Department>>> call, Throwable t) {
                Log.e("SelectFollowerActivity", "Department API call failed: " + t.getMessage());
                CustomToast.showCustomToast(SelectFollowerActivity.this, "Lỗi: " + t.getMessage());
            }
        });
    }

    private void updateSelectedCount() {
        tvSelectedCount.setText(String.valueOf(selectedFollowers.size()));
    }

    interface ResultCallback<T> {
        void onResult(T result);
    }
}