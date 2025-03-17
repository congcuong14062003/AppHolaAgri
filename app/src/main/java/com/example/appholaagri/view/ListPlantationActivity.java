package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.PlantationAdapter;
import com.example.appholaagri.adapter.PlantationListAdapter;
import com.example.appholaagri.adapter.RequestListAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.model.PlantationListModel.PlantationListResponse;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout; // Import thêm thư viện

public class ListPlantationActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private View progressBar;
    private LinearLayout emptyStateLayout;
    private PlantationListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ImageView backBtnReview;
    private static final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_plantation);

        recyclerView = findViewById(R.id.recyclerViewListPlantation);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        backBtnReview = findViewById(R.id.backBtnReview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout); // Thêm SwipeRefreshLayout

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gọi API ban đầu
        fetchRequests(currentPage);

        backBtnReview.setOnClickListener(view -> finish());

        // Xử lý khi kéo xuống để làm mới danh sách
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1; // Reset lại trang
            isLastPage = false;
            fetchRequests(currentPage);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE) {
                            currentPage++;
                            fetchRequests(currentPage);
                        }
                    }
                }
            }
        });
    }

    private void fetchRequests(int page) {
        isLoading = true;
        if (page == 1) {
            swipeRefreshLayout.setRefreshing(true); // Hiện trạng thái làm mới
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        ApiHelper.fetchListPlantation(this, page,
                data -> {
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);

                    if (data != null && data.getData() != null) {
                        if (page == 1) {
                            if (adapter != null) {
                                adapter.clearData();
                            }
                        }

                        if (data.getData().isEmpty()) {
                            isLastPage = true;
                            if (adapter == null || adapter.getItemCount() == 0) {
                                emptyStateLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            emptyStateLayout.setVisibility(View.GONE);
                            if (adapter == null) {
                                adapter = new PlantationListAdapter(data.getData());
                                adapter.setOnItemClickListener(plantationId -> {
                                    Intent intent = new Intent(this, PlantationDetailActivity.class);
                                    intent.putExtra("plantationId", plantationId);
                                    startActivity(intent);
                                });

                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.addData(data.getData());
                            }
                        }
                    }
                },
                errorMessage -> {
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Log.e("ListPlantationActivity", "Error: " + errorMessage);
                }
        );
    }
}
