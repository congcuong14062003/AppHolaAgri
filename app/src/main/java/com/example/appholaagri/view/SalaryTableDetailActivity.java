package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.DayAdapterBangCongDetail;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailData;
import com.example.appholaagri.model.SalaryTableDetailModel.SalaryTableDetailRequest;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryTableDetailActivity extends BaseActivity {
    TextView txt_cong_chuan, txt_tong_cong, txt_ngay_phep_con_lai,title_detail_salary;
    ImageView backBtnReview;
    LinearLayout container_salary_table_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salary_table_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_cong_chuan = findViewById(R.id.txt_cong_chuan);
        txt_tong_cong = findViewById(R.id.txt_tong_cong);
        txt_ngay_phep_con_lai = findViewById(R.id.txt_ngay_phep_con_lai);
        title_detail_salary = findViewById(R.id.title_detail_salary);
        backBtnReview = findViewById(R.id.backBtnReview);
        container_salary_table_detail = findViewById(R.id.container_salary_table_detail);
        container_salary_table_detail.setVisibility(View.GONE);
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });


        // Lấy Intent và dữ liệu được truyền từ Adapter
        Intent intent = getIntent();

        String code = intent.getStringExtra("workSummaryMonthly_code");
        int id = intent.getIntExtra("workSummaryMonthly_id", -1);
        String name = intent.getStringExtra("workSummaryMonthly_name");
        int status = intent.getIntExtra("workSummaryMonthly_status", -1);
        String displayDate = intent.getStringExtra("displayDate");
        title_detail_salary.setText(("TỔNG HỢP CÔNG " + displayDate).toUpperCase());
        callDetailApi(code, id, name, status);
    }
    private void callDetailApi(String code, int id, String name, int status) {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("AppPreferences", getBaseContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Tạo đối tượng WorkSummaryMonthly
        SalaryTableDetailRequest.WorkSummaryMonthly workSummaryMonthly =
                new SalaryTableDetailRequest.WorkSummaryMonthly(code, id, name, status);

        // Tạo request
        SalaryTableDetailRequest request = new SalaryTableDetailRequest();
        request.setKeySearch("");  // Giá trị tìm kiếm
        request.setPage(1);        // Trang đầu tiên
        request.setSize(20);       // Số lượng kết quả trả về
        request.setWorkSummaryMonthly(workSummaryMonthly);

        // Khởi tạo API interface
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        // Gọi API
        Call<ApiResponse<SalaryTableDetailData>> call = apiInterface.salaryTableDetailData(token, request);

        call.enqueue(new Callback<ApiResponse<SalaryTableDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SalaryTableDetailData>> call, Response<ApiResponse<SalaryTableDetailData>> response) {
                Log.d("SalaryTableDetailActivity", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SalaryTableDetailData> apiResponse = response.body();
                    if(apiResponse.getStatus() == 200) {
                        SalaryTableDetailData detailData = apiResponse.getData();
                        // Xử lý dữ liệu nhận được từ API
                        updatUiDetail(detailData);
                        container_salary_table_detail.setVisibility(View.VISIBLE);
                        Log.d("SalaryTableDetailActivity", "Detail Data: " + detailData.toString());
                    } else {

                    }
                } else {
                    Log.e("SalaryTableDetailActivity", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SalaryTableDetailData>> call, Throwable t) {
                Log.e("SalaryTableDetailActivity", "Error: " + t.getMessage());
            }
        });
    }
    private void updatUiDetail(SalaryTableDetailData detailData) {
        txt_cong_chuan.setText(detailData.getStandardWorkEfficient().toString().trim());
        txt_tong_cong.setText(detailData.getTotalWorkEfficient().toString().trim());
        txt_ngay_phep_con_lai.setText(detailData.getRemainDayOff().toString().trim());
        RecyclerView recyclerView = findViewById(R.id.recycler_view_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DayAdapterBangCongDetail dayAdapter = new DayAdapterBangCongDetail(detailData.getAttendanceSummaryRes());
        recyclerView.setAdapter(dayAdapter);
    }
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(SalaryTableDetailActivity.this, SalaryTableActivity.class);
        intent.putExtra("navigate_to", "home");
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }
}