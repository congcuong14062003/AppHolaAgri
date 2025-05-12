package com.example.appholaagri.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.MyWorkShiftAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.utils.CustomToast;

import java.util.ArrayList;

public class MyShiftFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MyWorkShiftAdapter adapter;
    private View progressBar;
    private LinearLayout emptyStateLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 20;
    private int tabId;
    private int totalRecords = 0; // Store total records from API response

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shift, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewListPlantation);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyWorkShiftAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Get tabId from Bundle
        if (getArguments() != null) {
            tabId = getArguments().getInt("tab_id", -1);
            Log.d("MyShiftFragment", "tabId: " + tabId);
        }

        // Initial API call
        fetchWorkShifts(currentPage);

        // Handle scroll for pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // Only trigger when scrolling down
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null && !isLoading && !isLastPage) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        Log.d("MyShiftFragment", "Scroll: visibleItemCount=" + visibleItemCount +
                                ", totalItemCount=" + totalItemCount +
                                ", firstVisibleItemPosition=" + firstVisibleItemPosition +
                                ", triggerPoint=" + (totalItemCount - 5));

                        // Trigger loading next page when near the end
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
                                && firstVisibleItemPosition >= 0) {
                            Log.d("MyShiftFragment", "Triggering fetchWorkShifts for page: " + (currentPage + 1));
                            fetchWorkShifts(currentPage + 1);
                        }
                    }
                }
            }
        });

        // Handle refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d("MyShiftFragment", "Refreshing, resetting to page 1");
            currentPage = 1;
            isLastPage = false;
            totalRecords = 0;
            adapter.clearData();
            fetchWorkShifts(currentPage);
        });

        return view;
    }

    private void fetchWorkShifts(int page) {
        if (isLoading) {
            Log.d("MyShiftFragment", "fetchWorkShifts skipped, isLoading=true");
            return;
        }
        isLoading = true;
        Log.d("MyShiftFragment", "Fetching work shifts for page: " + page);
        if (page == 1) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ApiHelper.fetchListWorkShift(
                getContext(),
                "", // keySearch
                4,  // day
                4,  // month
                2024, // year
                tabId, // type
                page,
                -1, // status
                response -> {
                    Log.d("MyShiftFragment", "API Response: Data size=" + (response.getData() != null ? response.getData().size() : 0) +
                            ", TotalRecord=" + response.getTotalRecord());

                    if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                        if (page == 1) {
                            adapter.updateData(response.getData());
                            emptyStateLayout.setVisibility(View.GONE);
                        } else {
                            adapter.addData(response.getData());
                        }

                        currentPage = page;
                        totalRecords = response.getTotalRecord();
                        int loadedItems = page * PAGE_SIZE;
                        isLastPage = loadedItems >= totalRecords;

                        Log.d("MyShiftFragment", "Page: " + page + ", Loaded: " + loadedItems +
                                ", Total: " + totalRecords + ", IsLastPage: " + isLastPage +
                                ", Data size: " + response.getData().size());
                    } else {
                        Log.d("MyShiftFragment", "No data received for page: " + page);
                        if (page == 1) {
                            emptyStateLayout.setVisibility(View.VISIBLE);
                            adapter.clearData();
                        }
                        isLastPage = true;
                    }

                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                },
                errorMessage -> {
                    Log.e("MyShiftFragment", "API Error: " + errorMessage);
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (page == 1) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                    CustomToast.showCustomToast(getContext(), errorMessage);
                }
        );
    }
}