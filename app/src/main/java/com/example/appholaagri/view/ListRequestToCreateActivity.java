package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ActionRequestDetailAdapter;
import com.example.appholaagri.adapter.GroupRequestListAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupRequest;
import com.example.appholaagri.model.RequestGroupModel.RequestGroupResponse;
import com.example.appholaagri.model.RequestModel.RequestListData;
import com.example.appholaagri.model.RequestModel.RequestListRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRequestToCreateActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroupRequestListAdapter adapter;
    private ImageView backBtnReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_request_to_create);
        recyclerView = findViewById(R.id.recyclerViewGroupRequest);
        backBtnReview = findViewById(R.id.backBtnReview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        adapter = new GroupRequestListAdapter(new ArrayList<>()); // Adapter ban đầu với danh sách rỗng
//        recyclerView.setAdapter(adapter);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        // Lấy dữ liệu từ API
        fetchData();
    }
    private void fetchData() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Tạo request object
        RequestGroupRequest request = new RequestGroupRequest(1, 20);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token == null) {
            Log.e("FetchData", "Token is null. Please ensure the user is logged in.");
            return;
        }

        // Gọi API
        Call<ApiResponse<RequestGroupResponse>> call = apiInterface.listRequestGroup(token, request);
        call.enqueue(new Callback<ApiResponse<RequestGroupResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestGroupResponse>> call, Response<ApiResponse<RequestGroupResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestGroupResponse> apiResponse = response.body();

                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        RequestGroupResponse requestGroup = apiResponse.getData();

                        // Cập nhật dữ liệu cho RecyclerView
                        List<RequestGroupResponse.RequestGroup> dataItems = requestGroup.getData();
                        if (dataItems != null) {
                            // Adapter cập nhật dữ liệu mới
//                            adapter.updateData(dataItems);
                            Log.d("FetchData", "Data loaded successfully. Total items: " + dataItems.size());

                            adapter = new GroupRequestListAdapter(dataItems);
                            adapter.setOnItemClickListener((GroupRequestId) -> {
                                Intent intent = new Intent(ListRequestToCreateActivity.this, CreateRequestActivity.class);
                                intent.putExtra("GroupRequestId", GroupRequestId);
                                startActivity(intent);
                            });
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    } else {
                        Log.e("FetchData", "API responded but with a non-200 status: " + apiResponse.getStatus());
                    }
                } else {
                    Log.e("FetchData", "Response is unsuccessful or body is null.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestGroupResponse>> call, Throwable t) {
                Log.e("FetchData", "API call failed: " + t.getMessage());
            }
        });
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(ListRequestToCreateActivity.this, RequestActivity.class);
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
}