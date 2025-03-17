package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.PlantationListAdapter;
import com.example.appholaagri.adapter.SensorListAdapter;
import com.example.appholaagri.helper.ApiHelper;

public class SensorFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private SensorListAdapter adapter;
    private EditText edtSearch;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ImageView backBtnReview;
    private static final int PAGE_SIZE = 20;
    private int plantationId;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private String keySearch = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);



        recyclerView = view.findViewById(R.id.recyclerViewListSensor);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        backBtnReview = view.findViewById(R.id.backBtnReview);
        edtSearch = view.findViewById(R.id.edtSearch);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            plantationId = getArguments().getInt("plantationId", -1);
            fetchRequests(currentPage, plantationId, keySearch);
            // 🔹 Xử lý plantationId ở đây
        }
        // Gọi API ban đầu
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            fetchRequests(currentPage, plantationId, keySearch);
        });
        backBtnReview.setOnClickListener(v -> {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
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
                            fetchRequests(currentPage, plantationId, keySearch);
                        }
                    }
                }
            }
        });

        // Xử lý tìm kiếm với delay tránh spam API
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keySearch = charSequence.toString().trim();
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> {
                    currentPage = 1;
                    fetchRequests(currentPage, plantationId, keySearch);
                };
                searchHandler.postDelayed(searchRunnable, 500); // Gọi API sau 500ms nếu không nhập tiếp
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }
    private void fetchRequests(int page, int plantationId, String keySearch) {
        if (page == 1) {
            swipeRefreshLayout.setRefreshing(false); // Tắt loading khi tải xong
        }

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        ApiHelper.fetchListSensor(getContext(), page, plantationId, keySearch,
                data -> {
                    isLoading = false;
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
                                adapter = new SensorListAdapter(data.getData());
                                adapter.setOnItemClickListener(monitoringId -> {
//                                     Chuyển sang PlantationDetailActivity và truyền plantationId
                                    Intent intent = new Intent(getContext(), MonitoringMeasurementPointActivity.class);
                                    intent.putExtra("monitoringId", monitoringId);
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
                    progressBar.setVisibility(View.GONE);
                    Log.e("ListPlantationActivity", "Error: " + errorMessage);
                }
        );
    }
}