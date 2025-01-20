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
import com.example.appholaagri.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatePickerFragment extends Fragment {
    private RecyclerView recyclerView;
    private TimekeepingAdapter adapter;
    private int currentPage = 1;
    private boolean isLoading = false; // Trạng thái tải dữ liệu
    private boolean isLastPage = false; // Đã tải hết dữ liệu hay chưa
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewMonth);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách chấm công hôm nay
        callTimekeepingApi("", currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // Đã cuộn đến cuối danh sách, tải thêm dữ liệu
                    if (!isLoading && !isLastPage) {
                        currentPage++;
                        callTimekeepingApi("", currentPage);
                    }
                }
            }
        });
        return view;
    }

    private void callTimekeepingApi(String date, int currentPage) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Tạo yêu cầu để gửi cho API
        TimekeepingStatisticsRequest request = new TimekeepingStatisticsRequest();
        request.setDate(date); // Truyền ngày đầu tiên của tháng, ví dụ "01/12/2024"
        request.setIsDaily(0); // Thống kê theo tháng
        request.setPage(currentPage);
        request.setSize(20);
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
                        if (data != null && data.getData() != null) {
                            if (data.getData().isEmpty()) {
                                isLastPage = true; // Không còn dữ liệu để tải
                            } else {
                                if (adapter == null) {
                                    adapter = new TimekeepingAdapter(data.getData());
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.addData(data.getData());
                                }
                            }
                        }
                    } else {
                        CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
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
        currentPage = 1;
        isLastPage = false;
        adapter = null;
        callTimekeepingApi(date, currentPage);  // Truyền ngày vào để gọi lại API với tháng mới
    }
}
