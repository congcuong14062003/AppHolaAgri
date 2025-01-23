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
import android.widget.LinearLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.SalaryTableAdapter;
import com.example.appholaagri.adapter.TimeKeepingManageRefusedAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableData;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;

public class BangCongFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private SalaryTableAdapter adapter;
    private LinearLayout emptyStateLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bang_cong, container, false);

        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewBangCong);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        // Gọi API để lấy danh sách chấm công hôm nay
        callSalaryTableApi();
        return view;
    }

    private void callSalaryTableApi() {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);

        // Tạo yêu cầu để gửi cho API
        SalaryTableRequest request = new SalaryTableRequest();
        request.setKeySearch("");  // Giá trị tìm kiếm
        request.setPage(1);        // Trang đầu tiên
        request.setSize(20);      // Số lượng kết quả trả về

        // Tham số khác có thể lấy từ SharedPreferences hoặc cứng định
        request.setCompanyId(Arrays.asList(-1));  // Công ty
        request.setDivisionId(Arrays.asList(-1));  // Phòng ban
        request.setMonth(-1);      // Tháng
        request.setStatus(Arrays.asList(-1));  // Trạng thái
        request.setYear(-1);       // Năm

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null); // Lấy token từ SharedPreferences

        // Gọi API
        Call<ApiResponse<SalaryTableData>> call = apiInterface.salaryTableData(token, request);

        call.enqueue(new Callback<ApiResponse<SalaryTableData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SalaryTableData>> call, Response<ApiResponse<SalaryTableData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SalaryTableData> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        SalaryTableData data = apiResponse.getData();
                        Log.d("BangCongFragment", "Dữ liệu: " + (data != null ? data.getData() : "null"));

                        // Kiểm tra dữ liệu trả về từ API
                        if (data != null && data.getData() != null && !data.getData().isEmpty()) {
                            // Cập nhật dữ liệu vào RecyclerView
                            adapter = new SalaryTableAdapter(data.getData());
                            recyclerView.setAdapter(adapter);
                        } else {
                            // Hiển thị empty state
                            recyclerView.setVisibility(View.GONE);
                            emptyStateLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CustomToast.showCustomToast(getContext(), apiResponse.getMessage());
                        Log.d("BangCongFragment", "Thông báo lỗi: " + apiResponse.getMessage());
                        // Hiển thị empty state
                        recyclerView.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Xử lý khi API trả về không thành công
                    Log.e("BangCongFragment", "API call failed or response body is null");
                    // Hiển thị empty state
                    recyclerView.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SalaryTableData>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                // Hiển thị empty state
                recyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
