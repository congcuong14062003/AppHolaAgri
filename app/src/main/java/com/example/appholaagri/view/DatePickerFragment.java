package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.RequestListAdapter;
import com.example.appholaagri.adapter.TimekeepingAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;

import java.util.ArrayList;

public class DatePickerFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private TimekeepingAdapter adapter;
    private int currentPage = 1;
    private ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentDate = "";
    private static final int PAGE_SIZE = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMonth);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progressBar);
        // Gọi API lần đầu
        fetchTimekeepingData(currentDate, currentPage);

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
                            fetchTimekeepingData(currentDate, currentPage);
                        }
                    }
                }
            }
        });


        return view;
    }

    private void fetchTimekeepingData(String date, int page) {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        ApiHelper.fetchTimekeepingStatistics(getContext(), date, page, 0,
                data -> {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    if (data != null && data.getData() != null) {
                        if (page == 1) {
                            // Lần đầu load dữ liệu, xóa danh sách cũ trong adapter
                            if (adapter != null) {
                                adapter.clearData();
                            }
                        }
                        if (data.getData().isEmpty()) {
                            isLastPage = true;
                            if (adapter == null || adapter.getItemCount() == 0) {
//                                emptyStateLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
//                            emptyStateLayout.setVisibility(View.GONE);
                            if (adapter == null) {
                                adapter = new TimekeepingAdapter(data.getData());

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
                    Log.e("LazyLoadFragment", "Error: " + errorMessage);
                }
        );
    }


    public void updateTimekeepingData(String date) {
        this.currentDate = date; // Cập nhật trạng thái
        currentPage = 1; // Reset lại trang hiện tại
        isLastPage = false; // Reset trạng thái phân trang
        if (adapter != null) {
            adapter.clearData(); // Xóa dữ liệu cũ
        }
        fetchTimekeepingData(currentDate, currentPage); // Gọi lại API với trạng thái mới
    }
}
