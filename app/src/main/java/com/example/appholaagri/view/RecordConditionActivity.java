package com.example.appholaagri.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.RecordConditionAdapterTabList;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionTabList;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordConditionActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecordConditionAdapterTabList recordConditionAdapterTabList;
    private Calendar selectedDate = Calendar.getInstance(); // Lưu giữ ngày tháng hiện tại
    private TextView tab2Title, title_request;
    private ImageView calendarIcon, backBtnReview, SearchBtnReview;
    private RequestTabListData requestTabListData;
    private View overlay_background;
    private EditText edtSearch;
    private ConstraintLayout overlay, overlay_filter_status_container;
    private LinearLayout create_request_btn;
    private int tabId = 1; // Giá trị mặc định là 0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_condition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0,0, 0);
            return insets;
        });
        // Kết nối các view
        tabLayout = findViewById(R.id.tabRecordCondition);
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
            // Mở màn hình quét QR
            Intent intent = new Intent(RecordConditionActivity.this, QRScannerActivity.class);
            startActivityForResult(intent, 100); // 100 là requestCode, có thể thay đổi tùy theo bạn
        });

        backBtnReview.setOnClickListener(view -> {
            finish();
        });
        // Thiết lập Adapter cho ViewPager
        viewPager.setAdapter(recordConditionAdapterTabList);
        backBtnReview.setOnClickListener(view -> {
            finish();
        });
        // Theo dõi sự kiện chuyển tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Xóa nội dung EditText khi tab thay đổi
                tabId = RecordConditionAdapterTabList.getTabIdAtPosition(position);
                Log.d("hahaha", "tabId: "+ tabId);
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
    }

    private void getInitFormData(String token) {
        Log.d("RecordConditionActivity", "Vàoooooo");
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<List<RecordConditionTabList>>> call = apiInterface.recordConditionTabList(token);
        call.enqueue(new Callback<ApiResponse<List<RecordConditionTabList>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<RecordConditionTabList>>> call, @NonNull Response<ApiResponse<List<RecordConditionTabList>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<RecordConditionTabList>> apiResponse = response.body();
                    Log.d("RecordConditionActivity", "response: " + apiResponse);

                    if (apiResponse.getStatus() == 200) {
                        List<RecordConditionTabList> tabList = apiResponse.getData();

                        // Kiểm tra nếu danh sách không có tab thực (chỉ có 1 item rỗng hoặc trống)
                        if (tabList == null || tabList.isEmpty() || (tabList.size() == 1 && tabList.get(0).getId() == -1)) {
                            Log.d("RecordConditionActivity", "Không có tab hợp lệ => chuyển màn khác");

                            // Ẩn giao diện tab
                            tabLayout.setVisibility(View.GONE);
                            viewPager.setVisibility(View.GONE);

//                            // Chuyển màn hình khác (ví dụ: MainActivity hoặc bạn có thể tuỳ chọn màn)
//                            Intent intent = new Intent(RecordConditionActivity.this, MainActivity.class); // hoặc màn hình bạn muốn
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
                        } else {
                            updateTabs(tabList);
                        }
                    }

                } else {
                    Log.e("RecordConditionActivity", "API response not successful or body is null");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<RecordConditionTabList>>> call, @NonNull Throwable t) {
                Log.e("RecordConditionActivity", "Error: " + t.getMessage());
            }
        });
    }
    private void updateTabs(List<RecordConditionTabList> recordConditionTabLists) {
        // Log danh sách tên tab
        for (RecordConditionTabList tabData : recordConditionTabLists) {
            Log.d("RecordConditionActivity", "Tab name: " + tabData.getName()); // Log tên của mỗi tab
        }

        // Cập nhật adapter với danh sách tab
        recordConditionAdapterTabList = new RecordConditionAdapterTabList(this, recordConditionTabLists);
        viewPager.setAdapter(recordConditionAdapterTabList);

        // Tạo danh sách tên tab từ danh sách requestTabListData
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(recordConditionTabLists.get(position).getName()); // Lấy tên tab từ dữ liệu API
        });
        tabLayoutMediator.attach();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra nếu là kết quả từ màn hình quét QR
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            // Lấy kết quả QR từ Intent
            String qrResult = data.getStringExtra("QR_RESULT");

            // Xử lý kết quả QR ở đây (ví dụ, bạn có thể gửi nó đến API hoặc cập nhật UI)
            Log.d("RecordConditionActivity", "qrResult: " + qrResult);
            if(qrResult != null) {
                Intent intent = new Intent(this, QrContentRecordCondition.class);
                RecordConditionTabList currentTab = recordConditionAdapterTabList.getTabList().get(viewPager.getCurrentItem());
                intent.putExtra("tab_data", currentTab); // Truyền object
                intent.putExtra("qrResult", qrResult);
                startActivity(intent);
            }
            // Quay lại màn hình trước sau khi nhận kết quả
            // Bạn có thể thực hiện các thao tác với kết quả QR tại đây

        }
    }

}