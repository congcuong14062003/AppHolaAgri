package com.example.appholaagri.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListStatusRequestAdapter;
import com.example.appholaagri.adapter.RequestAdapterTabList;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusData;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestNewFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RequestAdapterTabList requestAdapterTabList;
    private TextView title_request;
    private ImageView backBtnReview, SearchBtnReview;
    private View overlay_background;
    private EditText edtSearch;
    private ConstraintLayout overlay, overlay_filter_status_container;
    private LinearLayout create_request_btn;
    private int tabId = 1; // Giá trị mặc định là 0

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_new, container, false);

        // Kết nối các view
        tabLayout = view.findViewById(R.id.tabRequest);
        viewPager = view.findViewById(R.id.viewPagerRequest);
        backBtnReview = view.findViewById(R.id.backBtnReview);
        overlay_background = view.findViewById(R.id.overlay_background);
        SearchBtnReview = view.findViewById(R.id.SearchBtnReview);
        title_request = view.findViewById(R.id.title_request);
        edtSearch = view.findViewById(R.id.edtSearch);
        overlay = view.findViewById(R.id.overlay_filter_status);
        overlay_filter_status_container = view.findViewById(R.id.overlay_filter_status_container);
        create_request_btn = view.findViewById(R.id.create_request_btn);

        // Apply padding only to the main content, not the overlay
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            // Apply padding to the main content, excluding the overlay
            View contentView = view.findViewById(R.id.viewPagerRequest);
            if (contentView != null) {
                contentView.setPadding(0, 0, 0, bottomInset);
            }
            return insets;
        });

        // Ensure overlay is full-screen and not affected by insets
        ViewCompat.setOnApplyWindowInsetsListener(overlay, (v, insets) -> {
            // Do not apply insets to the overlay to ensure it covers the bottom bar
            return insets;
        });

        create_request_btn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ListRequestToCreateActivity.class);
            startActivity(intent);
        });

        edtSearch.setVisibility(View.GONE); // Ban đầu ẩn EditText
        // Sử dụng View.post() để đảm bảo width được đo đạc chính xác
        edtSearch.post(() -> {
            edtSearch.setTranslationX(edtSearch.getWidth()); // Đặt vị trí ngoài màn hình
        });

        // Xử lý sự kiện touch cho overlay_background
        overlay_background.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                int[] location = new int[2];
                overlay_filter_status_container.getLocationOnScreen(location);
                int containerX = location[0];
                int containerY = location[1];
                int containerWidth = overlay_filter_status_container.getWidth();
                int containerHeight = overlay_filter_status_container.getHeight();

                if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
                    overlay.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true;
        });

        // Ẩn hiện ô tìm kiếm
        SearchBtnReview.setOnClickListener(v -> {
            if (title_request.getVisibility() == View.VISIBLE) {
                title_request.setVisibility(View.GONE);
                edtSearch.setVisibility(View.VISIBLE);
                edtSearch.animate()
                        .translationX(0)
                        .setDuration(300)
                        .start();
            } else {
                edtSearch.animate()
                        .translationX(edtSearch.getWidth())
                        .setDuration(300)
                        .withEndAction(() -> {
                            edtSearch.setVisibility(View.GONE);
                            title_request.setVisibility(View.VISIBLE);
                        })
                        .start();
            }
        });

        edtSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= edtSearch.getWidth() - edtSearch.getCompoundDrawables()[2].getBounds().width()) {
                    edtSearch.setText("");
                    return true;
                }
            }
            return false;
        });

        // Thay đổi theo sự kiện onchange của input
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                int currentPage = viewPager.getCurrentItem();
                Fragment currentFragment = requestAdapterTabList.getFragmentAtPosition(currentPage);

                if (currentFragment instanceof SendToMeRequestFragment) {
                    ((SendToMeRequestFragment) currentFragment).updateKeySearch(newText);
                } else if (currentFragment instanceof IsendRequestFragment) {
                    ((IsendRequestFragment) currentFragment).updateKeySearch(newText);
                } else if (currentFragment instanceof FollowingRequestFragment) {
                    ((FollowingRequestFragment) currentFragment).updateKeySearch(newText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý nút back
        backBtnReview.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Theo dõi sự kiện chuyển tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabId = requestAdapterTabList.getTabIdAtPosition(position);
                edtSearch.setText("");

                if (edtSearch.getVisibility() == View.VISIBLE) {
                    edtSearch.animate()
                            .translationX(edtSearch.getWidth())
                            .setDuration(300)
                            .withEndAction(() -> {
                                edtSearch.setVisibility(View.GONE);
                                title_request.setVisibility(View.VISIBLE);
                            })
                            .start();
                }
            }
        });

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", requireContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        getInitFormData(token);

        // Lọc theo trạng thái
        ImageView filterIcon = view.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> {
            getStatusList(token);
        });

        return view;
    }

    private void getInitFormData(String token) {
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        RequestTabListRequest requestTabListRequest = new RequestTabListRequest(1, 20);
        Call<ApiResponse<RequestTabListDataResponse>> call = apiInterface.requestTabListData(requestTabListRequest);
        call.enqueue(new Callback<ApiResponse<RequestTabListDataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<RequestTabListDataResponse>> call, Response<ApiResponse<RequestTabListDataResponse>> response) {
                Log.d("RequestNewFragment", "response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestTabListDataResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        List<RequestTabListData> tabList = apiResponse.getData().getData();
                        updateTabs(tabList);
                    } else {
                        Log.e("RequestNewFragment", "API response unsuccessful");
                    }
                } else {
                    Log.e("RequestNewFragment", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestTabListDataResponse>> call, Throwable t) {
                Log.e("RequestNewFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void updateTabs(List<RequestTabListData> requestTabListData) {
        for (RequestTabListData tabData : requestTabListData) {
            Log.d("RequestNewFragment", "Tab name: " + tabData.getName());
        }

        requestAdapterTabList = new RequestAdapterTabList(getActivity(), requestTabListData);
        viewPager.setAdapter(requestAdapterTabList);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(requestTabListData.get(position).getName());
        });
        tabLayoutMediator.attach();
    }

    private void showStatusOverlay(List<RequestStatusData> statusList) {
        overlay.setVisibility(View.VISIBLE);
        overlay_background.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = overlay.findViewById(R.id.recycler_filter_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ListStatusRequestAdapter adapter = new ListStatusRequestAdapter(statusList, status -> {
            overlay.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
            Log.d("RequestNewFragment", "Selected status: " + status.getId());
            int currentPage = viewPager.getCurrentItem();
            Fragment currentFragment = requestAdapterTabList.getFragmentAtPosition(currentPage);

            if (currentFragment instanceof SendToMeRequestFragment) {
                ((SendToMeRequestFragment) currentFragment).updateStatus(status.getId());
            } else if (currentFragment instanceof IsendRequestFragment) {
                ((IsendRequestFragment) currentFragment).updateStatus(status.getId());
            } else if (currentFragment instanceof FollowingRequestFragment) {
                ((FollowingRequestFragment) currentFragment).updateStatus(status.getId());
            }
        });

        recyclerView.setAdapter(adapter);

        Button closeButton = overlay.findViewById(R.id.button_close_overlay);
        closeButton.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });
    }

    private void getStatusList(String token) {
        RequestStatusRequest requestStatusRequest = new RequestStatusRequest();
        requestStatusRequest.setRequestType(tabId);
        requestStatusRequest.setPage(1);
        requestStatusRequest.setSize(20);
        requestStatusRequest.setKeySearch("");
        requestStatusRequest.setStatus(-1);
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        Call<ApiResponse<RequestStatusResponse>> call = apiInterface.requestStatusData(token, requestStatusRequest);
        call.enqueue(new Callback<ApiResponse<RequestStatusResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestStatusResponse>> call, Response<ApiResponse<RequestStatusResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestStatusResponse> apiResponse = response.body();
                    RequestStatusResponse responseData = apiResponse.getData();
                    if (responseData != null && responseData.getData() != null) {
                        List<RequestStatusData> statusList = responseData.getData();
                        showStatusOverlay(statusList);
                    } else {
                        Log.e("RequestNewFragment", "Response data or status list is null.");
                    }
                } else {
                    Log.e("RequestNewFragment", "Failed to fetch statuses.");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<RequestStatusResponse>> call, Throwable t) {
                Log.e("RequestNewFragment", "Error: " + t.getMessage());
            }
        });
    }
}