package com.example.appholaagri.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.RequestListAdapter;
import com.example.appholaagri.helper.ApiHelper;


public class FollowingRequestFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private int tabId = 0; // Giá trị mặc định là 0
    private int statusId = -1; // Giá trị mặc định là -1
    private String keySearch = ""; // Giá trị mặc định là "
    private RequestListAdapter adapter;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int PAGE_SIZE = 20;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabId = getArguments().getInt("tab_id", 0); // Lấy ID của tab từ arguments
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following_request, container, false);
        recyclerView = view.findViewById(R.id.recyclerFollowingRequest);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Xử lý sự kiện kéo để làm mới
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            if (adapter != null) {
                adapter.clearData();
            }
            fetchRequests(currentPage, tabId);
            swipeRefreshLayout.setRefreshing(false);
        });
        // Gọi API ban đầu
        fetchRequests(currentPage, tabId);
        // Gọi API ban đầu
        fetchRequests(currentPage, tabId);

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
                            fetchRequests(currentPage,tabId);
                        }
                    }
                }
            }
        });
        return view;
    }
    private void fetchRequests(int page, int tabId) {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        ApiHelper.fetchRequestListData(getContext(), tabId, page, keySearch, statusId,
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
                                adapter = new RequestListAdapter(data.getData());
                                adapter.setOnItemClickListener((requestId, StatusRequest, groupRequestCode, GroupRequest) -> {
                                    Log.d("IsendRequestFragment", "requestId: " + requestId);
                                    Log.d("IsendRequestFragment", "StatusRequest: " + StatusRequest);
                                    Log.d("IsendRequestFragment", "GroupRequest: " + GroupRequest);
                                    if (groupRequestCode.equals("LATE_EARLY")) {
                                        // đi muộn về sớm
                                        Intent intent = new Intent(getContext(), CreateRequestLateEarlyActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    } else if (groupRequestCode.equals("DAY_OFF")) {
                                        // xin nghỉ phép
                                        Intent intent = new Intent(getContext(), CreateRequestDayOffActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("OVER_TIME")) {
                                        // xin làm thêm
                                        Intent intent = new Intent(getContext(), CreateRequestOvertTimeActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("BUY_NEW")) {
                                        // mua sắm vật tư
                                        Intent intent = new Intent(getContext(), CreateRequestBuyNewActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    } else if (groupRequestCode.equals("RESIGN")) {
                                        // xin thôi việc
                                        Intent intent = new Intent(getContext(), CreateRequestResignActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("BUSINESS_TRIP")) {
                                        // Tờ trình công tác
                                        Intent intent = new Intent(getContext(), CreateRequestWorkReportActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }

                                    else if (groupRequestCode.equals("EXPENDITURE")) {
                                        // Tờ trình khác
                                        Intent intent = new Intent(getContext(), CreateRequestAnotherSubmissionActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("CUSTOMER_CONTRACT")) {
                                        // Phê duyệt hợp đồng
                                        Intent intent = new Intent(getContext(), CreateRequestContractApprovalActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("CUSTOMER_CONTRACT_BRAND_NAME")) {
                                        // Phê duyệt hợp đồng SMS
                                        Intent intent = new Intent(getContext(), CreateRequestContractApprovalSmSActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("RECRUITMENT")) {
                                        // Tờ trình tuyển dụng
                                        Intent intent = new Intent(getContext(), CreateRequestRecruitmentFlyerActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("EMPLOYEE_TRANSITION")) {
                                        // Tờ trình nhân sự
                                        Intent intent = new Intent(getContext(), CreateRequestPersonnelReportActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
                                    else if (groupRequestCode.equals("POLICY")) {
                                        // Tờ trình chính sách, quy trình, quy định
                                        Intent intent = new Intent(getContext(), CreateRequestPolicyRegulationSubmissionActivity.class);
                                        intent.putExtra("requestId", requestId);
                                        intent.putExtra("StatusRequest", StatusRequest);
                                        intent.putExtra("GroupRequest", GroupRequest);
                                        startActivity(intent);
                                    }
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
                    Log.e("LazyLoadFragment", "Error: " + errorMessage);
                }
        );
    }
    public void updateStatus(int newStatusId) {
        Log.d("SendToMeRequestFragment", "Status selected: " + newStatusId);
        this.statusId = newStatusId; // Cập nhật trạng thái
        currentPage = 1; // Reset lại trang hiện tại
        isLastPage = false; // Reset trạng thái phân trang
        if (adapter != null) {
            adapter.clearData(); // Xóa dữ liệu cũ
        }
        fetchRequests(currentPage, tabId); // Gọi lại API với trạng thái mới
    }
    public void updateKeySearch(String newKeySearch) {
        Log.d("SendToMeRequestFragment", "Status selected: " + keySearch);
        this.keySearch = newKeySearch; // Cập nhật trạng thái
        currentPage = 1; // Reset lại trang hiện tại
        isLastPage = false; // Reset trạng thái phân trang
        if (adapter != null) {
            adapter.clearData(); // Xóa dữ liệu cũ
        }
        fetchRequests(currentPage, tabId); // Gọi lại API với trạng thái mới
    }
}