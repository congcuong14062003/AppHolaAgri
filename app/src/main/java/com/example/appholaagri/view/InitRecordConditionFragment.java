package com.example.appholaagri.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.RecordConditionAdapter;
import com.example.appholaagri.helper.ApiHelper;
import com.example.appholaagri.helper.RecordConditionHelper;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitRecordConditionFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecordConditionAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_init_record_condition, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewListPlantation);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecordConditionAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        // Lấy tabId từ Bundle
        if (getArguments() != null) {
            tabId = getArguments().getInt("tab_id", -1);  // Giá trị mặc định là -1 nếu không có tabId
        }
        // Gọi API lần đầu
        getListRecordCondition(currentPage, tabId);

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
                        getListRecordCondition(currentPage + 1, tabId);
                    }
                }
            }
        });

        // Xử lý refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            adapter.clearData();
            getListRecordCondition(currentPage, tabId);
        });

        return view;
    }

    private void getListRecordCondition(int page, int status) {

        ApiHelper.fetchRecordCondition(
                getContext(),
                page,
                status,
                response -> {
                    if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                        // Cập nhật danh sách và tăng currentPage
                        if (page == 1) {
                            adapter.updateData(response.getData());
                        } else {
                            adapter.addData(response.getData());
                        }

                        currentPage = page;
                        isLoading = false;

                        // Nếu không còn dữ liệu, set isLastPage = true
                        if (response.getData().size() < PAGE_SIZE) {
                            isLastPage = true;
                        }

                        // Cập nhật giao diện (ẩn progressBar, hiển thị danh sách)
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        emptyStateLayout.setVisibility(View.GONE);

                        // Kiểm tra trạng thái rỗng
//                        emptyStateLayout.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    } else {
                        Log.d("InitRecordConditionFragment", "vào" + adapter.getItemCount());
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                },
                errorMessage -> {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    CustomToast.showCustomToast(getContext(), errorMessage);
                    Log.e("InitRecordConditionFragment", errorMessage);
                }
        );
    }
}
