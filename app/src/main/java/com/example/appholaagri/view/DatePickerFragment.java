package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.TimekeepingAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsData;
import com.example.appholaagri.model.TimekeepingStatisticsModel.TimekeepingStatisticsRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatePickerFragment extends Fragment {
    private RecyclerView recyclerView;
    private TimekeepingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMonth);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách chấm công hôm nay
        callTimekeepingApi("");
        return view;
    }

    private void callTimekeepingApi(String date) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Tạo yêu cầu để gửi cho API
        TimekeepingStatisticsRequest request = new TimekeepingStatisticsRequest();
        request.setDate(date); // Truyền ngày đầu tiên của tháng, ví dụ "01/12/2024"
        request.setIsDaily(0); // Thống kê theo tháng
        request.setPage(1);
        request.setSize(100);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
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
                        Log.d("TimekeepingFragment", "Data: " + (data != null ? data.getData() : "null"));

                        // Kiểm tra dữ liệu trả về từ API
                        if (data != null && data.getData() != null && !data.getData().isEmpty()) {
                            // Cập nhật dữ liệu vào RecyclerView
                            adapter = new TimekeepingAdapter(data.getData());
                            recyclerView.setAdapter(adapter);
                        } else {
                            // Xử lý khi dữ liệu trống hoặc không hợp lệ
                            Log.e("TimekeepingFragment", "No data available or data is empty");
                        }
                    } else {
                        Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("LoginActivity", "Error message: " + apiResponse.getMessage());
                    }
                } else {
                    // Xử lý khi API trả về không thành công
                    Log.e("TimekeepingFragment", "API call failed or response body is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TimekeepingStatisticsData>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    // Hàm cập nhật lại dữ liệu khi người dùng chọn tháng
    public void updateTimekeepingData(String date) {
        callTimekeepingApi(date);  // Truyền ngày vào để gọi lại API với tháng mới
    }
}
