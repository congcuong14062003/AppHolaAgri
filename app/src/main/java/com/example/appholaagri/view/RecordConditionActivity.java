package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.RecordConditionAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionRequest;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordConditionActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecordConditionAdapter adapter;
    private View progressBar;
    private LinearLayout emptyStateLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView backBtnReview;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_condition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0,0, 0);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewListPlantation);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        backBtnReview = findViewById(R.id.backBtnReview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordConditionAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Gọi API lần đầu
        getListRecordCondition(currentPage);

        // Xử lý khi cuộn đến cuối danh sách
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        // Cuộn đến cuối danh sách, tải thêm dữ liệu
                        getListRecordCondition(currentPage + 1);
                    }
                }
            }
        });

        // Xử lý refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            adapter.clearData();
            getListRecordCondition(currentPage);
        });


        backBtnReview.setOnClickListener(view -> {
            finish();
        });
    }

    public void getListRecordCondition(int page) {
        if (isLoading) return;

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        // Tạo request object
        RecordConditionRequest request = new RecordConditionRequest(
                Collections.singletonList(-1), "", "", page,
                Collections.singletonList(-1), PAGE_SIZE, "",
                Collections.singletonList(-1), Collections.singletonList(-1)
        );

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Gọi API
        Call<ApiResponse<RecordConditionResponse>> call = apiInterface.recordCondition(token, request);

        call.enqueue(new Callback<ApiResponse<RecordConditionResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RecordConditionResponse>> call, Response<ApiResponse<RecordConditionResponse>> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RecordConditionResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        RecordConditionResponse data = apiResponse.getData();

                        if (data != null && data.getData() != null && !data.getData().isEmpty()) {
                            adapter.addData(data.getData());
                            currentPage = page;

                            if (data.getData().size() < PAGE_SIZE) {
                                isLastPage = true;
                            }
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        isLastPage = true;
                        CustomToast.showCustomToast(getBaseContext(), apiResponse.getMessage());
                    }
                } else {
                    isLastPage = true;
                    Log.e("API Error", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RecordConditionResponse>> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                isLastPage = true;

                Log.e("API Error", t.getMessage());
            }
        });
    }
}