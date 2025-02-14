package com.example.appholaagri.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ListStatusRequestAdapter;
import com.example.appholaagri.adapter.RequestAdapterTabList;
import com.example.appholaagri.adapter.TimekeepingManageAdapterTabList;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.CheckInInitFormData.CheckInInitFormData;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusData;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusRequest;
import com.example.appholaagri.model.RequestStatusModel.RequestStatusResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RequestAdapterTabList requestAdapterTabList;
    private Calendar selectedDate = Calendar.getInstance(); // Lưu giữ ngày tháng hiện tại
    private TextView tab2Title, title_request;
    private ImageView calendarIcon, backBtnReview, SearchBtnReview;
    private RequestTabListData requestTabListData;
    private View overlay_background;
    private EditText edtSearch;
    private ConstraintLayout overlay, overlay_filter_status_container;
    private LinearLayout create_request_btn;
    private int tabId = 1; // Giá trị mặc định là 0
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request);
        // Kết nối các view
        tabLayout = findViewById(R.id.tabRequest);
        viewPager = findViewById(R.id.viewPagerRequest);
        backBtnReview = findViewById(R.id.backBtnReview);
        overlay_background = findViewById(R.id.overlay_background);
        SearchBtnReview = findViewById(R.id.SearchBtnReview);
        title_request = findViewById(R.id.title_request);
        edtSearch = findViewById(R.id.edtSearch);
        overlay = findViewById(R.id.overlay_filter_status);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);
        create_request_btn = findViewById(R.id.create_request_btn);

        create_request_btn.setOnClickListener(view -> {
            Intent intent = new Intent(RequestActivity.this, ListRequestToCreateActivity.class);
            startActivity(intent);
            finish();
        });


        edtSearch.setVisibility(View.GONE); // Ban đầu ẩn EditText
        // Sử dụng View.post() để đảm bảo width được đo đạc chính xác
        edtSearch.post(() -> {
            edtSearch.setTranslationX(edtSearch.getWidth()); // Đặt vị trí ngoài màn hình
        });

        // Add this in your onCreate method after initializing views:
        overlay_background.setOnTouchListener((view, event) -> {
            // Check if the touch event is outside the overlay_filter_status_container
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Get the coordinates of the touch
                float x = event.getX();
                float y = event.getY();

                // Check if the touch is outside the overlay_filter_status_container
                int[] location = new int[2];
                overlay_filter_status_container.getLocationOnScreen(location);
                int containerX = location[0];
                int containerY = location[1];
                int containerWidth = overlay_filter_status_container.getWidth();
                int containerHeight = overlay_filter_status_container.getHeight();

                if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
                    // Touch is outside the container, so hide the overlay
                    overlay.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true; // Handle the touch event and prevent further propagation
        });

        // ẩn hiện ô tìm kiếm
        SearchBtnReview.setOnClickListener(view -> {
            if (title_request.getVisibility() == View.VISIBLE) {
                // Ẩn tiêu đề và hiển thị EditText với animation
                title_request.setVisibility(View.GONE);

                edtSearch.setVisibility(View.VISIBLE);
                edtSearch.animate()
                        .translationX(0) // Trượt vào màn hình
                        .setDuration(300) // Thời gian animation
                        .start();
            } else {
                // Ẩn EditText với animation
                edtSearch.animate()
                        .translationX(edtSearch.getWidth()) // Trượt ra ngoài màn hình
                        .setDuration(300)
                        .withEndAction(() -> {
                            edtSearch.setVisibility(View.GONE); // Ẩn EditText sau animation
                            title_request.setVisibility(View.VISIBLE); // Hiển thị lại tiêu đề
                        })
                        .start();
            }
        });


        edtSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Kiểm tra nếu người dùng chạm vào vùng xóa (drawableRight)
                if (event.getX() >= edtSearch.getWidth() - edtSearch.getCompoundDrawables()[2].getBounds().width()) {
                    // Xóa nội dung trong EditText
                    edtSearch.setText("");
                    return true; // Xử lý sự kiện chạm và trả lại true để không tiếp tục xử lý thêm
                }
            }
            return false; // Nếu không chạm vào vùng xóa, không xử lý gì thêm
        });

        // thay đổi theo sự kiện onchange của input
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                // Truy cập và gọi hàm updateStatus
                int currentPage = viewPager.getCurrentItem();
                Fragment currentFragment = requestAdapterTabList.getFragmentAtPosition(currentPage);

                if (currentFragment instanceof SendToMeRequestFragment) {
                    ((SendToMeRequestFragment) currentFragment).updateKeySearch(newText);
                } else if (currentFragment instanceof IsendRequestFragment) {
                    ((IsendRequestFragment) currentFragment).updateKeySearch(newText);
                } else if (currentFragment instanceof  FollowingRequestFragment) {
                    ((FollowingRequestFragment) currentFragment).updateKeySearch(newText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Thiết lập Adapter cho ViewPager
        viewPager.setAdapter(requestAdapterTabList);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });

        // Theo dõi sự kiện chuyển tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Xóa nội dung EditText khi tab thay đổi
                tabId = requestAdapterTabList.getTabIdAtPosition(position);
                edtSearch.setText("");

                // Ẩn EditText và hiển thị lại tiêu đề nếu đang ở chế độ tìm kiếm
                if (edtSearch.getVisibility() == View.VISIBLE) {
                    edtSearch.animate()
                            .translationX(edtSearch.getWidth()) // Trượt EditText ra ngoài màn hình
                            .setDuration(300)
                            .withEndAction(() -> {
                                edtSearch.setVisibility(View.GONE); // Ẩn EditText sau animation
                                title_request.setVisibility(View.VISIBLE); // Hiển thị lại tiêu đề
                            })
                            .start();
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        getInitFormData(token);


        // lọc theo trạng thái
        ImageView filterIcon = findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> {
            getStatusList(token); // Gọi API lấy danh sách trạng thái
        });

    }
    private void getInitFormData(String token) {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        RequestTabListRequest requestTabListRequest = new RequestTabListRequest(1, 20);
        Call<ApiResponse<RequestTabListDataResponse>> call = apiInterface.requestTabListData(requestTabListRequest);
        call.enqueue(new Callback<ApiResponse<RequestTabListDataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<RequestTabListDataResponse>> call, Response<ApiResponse<RequestTabListDataResponse>> response) {
                Log.d("RequestActivity", "respose: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestTabListDataResponse> apiResponse = response.body();
                    if(apiResponse.getStatus() == 200) {
                        List<RequestTabListData> tabList = apiResponse.getData().getData(); // Lấy danh sách tab
                        int numOfRecords = apiResponse.getData().getNumOfRecords();
                        updateTabs(tabList); // Cập nhật danh sách tab vào Activity
                    } else {
                        Log.e("RequestActivity", "API response unsuccessful");
                    }
                } else {
                    Log.e("RequestActivity", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestTabListDataResponse>> call, Throwable t) {
                Log.e("RequestActivity", "Error: " + t.getMessage());
            }
        });
    }



    private void updateTabs(List<RequestTabListData> requestTabListData) {
        // Log danh sách tên tab
        for (RequestTabListData tabData : requestTabListData) {
            Log.d("RequestActivity", "Tab name: " + tabData.getName()); // Log tên của mỗi tab
        }

        // Cập nhật adapter với danh sách tab
        requestAdapterTabList = new RequestAdapterTabList(this, requestTabListData);
        viewPager.setAdapter(requestAdapterTabList);

        // Tạo danh sách tên tab từ danh sách requestTabListData
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(requestTabListData.get(position).getName()); // Lấy tên tab từ dữ liệu API
        });
        tabLayoutMediator.attach();
    }

    private void showStatusOverlay(List<RequestStatusData> statusList) {
        // Hiển thị overlay
        overlay.setVisibility(View.VISIBLE);
        overlay_background.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = findViewById(R.id.recycler_filter_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ListStatusRequestAdapter adapter = new ListStatusRequestAdapter(statusList, status -> {
            // Đóng overlay
            overlay.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
            Log.d("RequestActivity", "Selected status: " + status.getId());
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

        // Nút đóng
        Button closeButton = findViewById(R.id.button_close_overlay);
        closeButton.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });
    }

    private void getStatusList(String token) {

        RequestStatusRequest requestStatusRequest = new RequestStatusRequest();
        requestStatusRequest.setRequestType(tabId);
        requestStatusRequest.setPage(1);
        requestStatusRequest.setSize(20); // Bạn có thể chỉnh kích thước nếu cần
        requestStatusRequest.setKeySearch("");
        requestStatusRequest.setStatus(-1);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestStatusResponse>> call = apiInterface.requestStatusData(token, requestStatusRequest);
        call.enqueue(new Callback<ApiResponse<RequestStatusResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestStatusResponse>> call, Response<ApiResponse<RequestStatusResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<RequestStatusResponse> apiResponse = response.body();
                    List<RequestStatusData> statusList = apiResponse.getData().getData();
                    showStatusOverlay(statusList);
                } else {
                    Log.e("RequestActivity", "Failed to fetch statuses.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestStatusResponse>> call, Throwable t) {
                Log.e("RequestActivity", "Error: " + t.getMessage());
            }
        });
    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RequestActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();
    }

    public void ChangeStatusList() {

    }
}