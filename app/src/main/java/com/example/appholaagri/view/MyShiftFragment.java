package com.example.appholaagri.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.MyWorkShiftAdapter;
import com.example.appholaagri.adapter.RecordConditionAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.helper.RecordConditionHelper;
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
    private int tabId;  // Biến để lưu trữ tabId

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_shift, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewListPlantation);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyWorkShiftAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        // Lấy tabId từ Bundle
        if (getArguments() != null) {
            tabId = getArguments().getInt("tab_id", -1);  // Giá trị mặc định là -1 nếu không có tabId
        }
        // Gọi API lần đầu
        getListRecordCondition(4, 4, 2024, tabId, currentPage, -1);

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
                        getListRecordCondition(4, 4, 2024, tabId, currentPage + 1,-1);
                    }
                }
            }
        });

        // Xử lý refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            adapter.clearData();
            getListRecordCondition(4, 4, 2024, tabId, currentPage, -1);
        });

        return view;
    }

    private void getListRecordCondition(int day, int month, int year, int type, int page, int status) {

        ApiHelper.fetchListWorkShift(
                getContext(),
                "", // keySearch nếu không có
                day,
                month,
                year,
                type,
                page,
                status,
                response -> {
                    if (response != null && response != null && !response.isEmpty()) {
                        if (page == 1) {
                            adapter.updateData(response);
                        } else {
                            adapter.addData(response);
                        }

                        currentPage = page;
                        isLoading = false;

                        if (response.size() < PAGE_SIZE) {
                            isLastPage = true;
                        }

                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        emptyStateLayout.setVisibility(View.GONE);
                    } else {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                },
                errorMessage -> {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    CustomToast.showCustomToast(getContext(), errorMessage);
                    Log.e("MyShiftFragment", errorMessage);
                }
        );

    }
}