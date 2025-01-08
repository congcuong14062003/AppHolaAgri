package com.example.appholaagri.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ViewPagerAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimekeepingStatisticsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Calendar selectedDate = Calendar.getInstance(); // Lưu giữ ngày tháng hiện tại
    private TextView tab2Title;
    private ImageView calendarIcon, backBtnReview;
    private LinearLayout quanlychamcong_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timekeeping_statistics);

        // Kết nối các view
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        backBtnReview = findViewById(R.id.backBtnReview);
        quanlychamcong_btn = findViewById(R.id.quanlychamcong_btn);
        // Thiết lập Adapter cho ViewPager
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });
        quanlychamcong_btn.setOnClickListener(view -> {
            Intent intent = new Intent(TimekeepingStatisticsActivity.this, TimekeepingManagementActivity.class);
            startActivity(intent);
            finish();
        });
        // Liên kết TabLayout với ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Hôm nay");
                    break;
                case 1:
                    // Sử dụng layout tùy chỉnh cho tab thứ 2
                    View customTabView = LayoutInflater.from(this).inflate(R.layout.tab_title_layout, null);
                    tab.setCustomView(customTabView);

                    // Lấy tham chiếu đến các view trong layout tùy chỉnh
                    tab2Title = customTabView.findViewById(R.id.tab2Title);
                    calendarIcon = customTabView.findViewById(R.id.calendarIcon);

                    // Cập nhật tiêu đề tab thứ 2
                    updateTabDate();

                    // Thiết lập sự kiện click cho icon lịch
                    calendarIcon.setOnClickListener(v -> showDatePicker());

                    // Gọi API với ngày mặc định là tháng hiện tại (ngày 1 của tháng hiện tại)
                    callTimekeepingApi("");  // Truyền chuỗi rỗng để API lấy dữ liệu mặc định cho tháng hiện tại
                    break;
            }
        }).attach();
    }

    private void updateTabDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        String currentDateDisplay = sdf.format(selectedDate.getTime());
        tab2Title.setText("Tháng " + currentDateDisplay);
    }

    private void showDatePicker() {
        // Tạo dialog
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_month_year_picker);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Kết nối các view trong dialog
        NumberPicker monthPicker = dialog.findViewById(R.id.monthPicker);
        NumberPicker yearPicker = dialog.findViewById(R.id.yearPicker);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        // Thiết lập giá trị cho NumberPicker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(true);
        monthPicker.setValue(selectedDate.get(Calendar.MONTH) + 1);

        yearPicker.setMinValue(2000); // Giá trị năm tối thiểu
        yearPicker.setMaxValue(2100); // Giá trị năm tối đa
        yearPicker.setWrapSelectorWheel(true);
        yearPicker.setValue(selectedDate.get(Calendar.YEAR));

        // Xử lý sự kiện khi bấm nút "Xác nhận"
        btnConfirm.setOnClickListener(v -> {
            int selectedMonth = monthPicker.getValue();
            int selectedYear = yearPicker.getValue();

            // Cập nhật selectedDate
            selectedDate.set(Calendar.MONTH, selectedMonth - 1);
            selectedDate.set(Calendar.YEAR, selectedYear);

            // Cập nhật tiêu đề tab
            updateTabDate();

            // Truyền ngày mới cho DatePickerFragment
            String selectedDateStr = "01/" + selectedMonth + "/" + selectedYear;
            DatePickerFragment fragment = (DatePickerFragment) getSupportFragmentManager()
                    .findFragmentByTag("f1"); // "f1" là tag của fragment
            if (fragment != null) {
                fragment.updateTimekeepingData(selectedDateStr); // Gọi hàm cập nhật dữ liệu
            }

            dialog.dismiss(); // Đóng dialog
        });

        // Hiển thị dialog
        dialog.show();
    }

    // Gọi API để lấy dữ liệu mặc định cho tháng hiện tại
    private void callTimekeepingApi(String date) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Tạo yêu cầu để gửi cho API
        TimekeepingStatisticsRequest request = new TimekeepingStatisticsRequest();
        request.setDate(date); // Chuỗi rỗng nghĩa là lấy dữ liệu cho tháng hiện tại
        request.setIsDaily(0); // Thống kê theo tháng
        request.setPage(1);
        request.setSize(20);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Gọi API
        Call<ApiResponse<TimekeepingStatisticsData>> call = apiInterface.timekeepingStatistics(token, request);

        call.enqueue(new Callback<ApiResponse<TimekeepingStatisticsData>>() {
            @Override
            public void onResponse(Call<ApiResponse<TimekeepingStatisticsData>> call, Response<ApiResponse<TimekeepingStatisticsData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<TimekeepingStatisticsData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        TimekeepingStatisticsData data = apiResponse.getData();
                        Log.d("Timekeeping", "Data theo tháng hiện tại: " + (data != null ? data.getData() : "null"));

                        // Cập nhật dữ liệu vào RecyclerView
                        if (data != null && data.getData() != null && !data.getData().isEmpty()) {
                            DatePickerFragment fragment = (DatePickerFragment) getSupportFragmentManager()
                                    .findFragmentByTag("f1");
                            if (fragment != null) {
                                fragment.updateTimekeepingData(""); // Truyền dữ liệu vào fragment
                            }
                        } else {
                            Log.e("Timekeeping", "Không có dữ liệu");
                        }
                    } else {
                        Log.e("Timekeeping", "API trả về lỗi: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e("Timekeeping", "API call failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TimekeepingStatisticsData>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(TimekeepingStatisticsActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "home"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
}
