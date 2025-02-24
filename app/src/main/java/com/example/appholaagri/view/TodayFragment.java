package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimekeepingAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TimekeepingAdapter adapter;
    private int currentPage = 1;
    private ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentDate = "";
    private static final int PAGE_SIZE = 20;
    private LinearLayout emptyStateLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewToday);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        // Gọi API để lấy danh sách chấm công hôm nay
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

        ApiHelper.fetchTimekeepingStatistics(getContext(), date, page, 1,
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
                                emptyStateLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            emptyStateLayout.setVisibility(View.GONE);
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
}
