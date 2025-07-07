package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.SalaryTableAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableRequest;
import com.example.appholaagri.model.SalaryTableModel.SalaryTableResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.LoadingDialog;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BangLuongFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private SalaryTableAdapter adapter;
    private LinearLayout emptyStateLayout;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bang_luong, container, false);
        loadingDialog = new LoadingDialog(getContext());
        // Kết nối RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewBangLuong);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        // Gọi API để lấy danh sách chấm công hôm nay
        callSalaryTableApi();
        return view;
    }

    private void callSalaryTableApi() {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        loadingDialog.show();
        // Tạo yêu cầu để gửi cho API
        SalaryTableRequest request = new SalaryTableRequest();
        request.setIsApp(1);
        request.setKeySearch("");  // Giá trị tìm kiếm
        request.setPage(1);        // Trang đầu tiên
        request.setSize(20);      // Số lượng kết quả trả về
        request.setStatus(Arrays.asList(-1));  // Trạng thái

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null); // Lấy token từ SharedPreferences

        // Gọi API
        Call<ApiResponse<SalaryTableResponse>> call = apiInterface.salaryTableData(token, request);

        call.enqueue(new Callback<ApiResponse<SalaryTableResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SalaryTableResponse>> call, Response<ApiResponse<SalaryTableResponse>> response) {
                loadingDialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<SalaryTableResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        SalaryTableResponse data = apiResponse.getData();
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
                        recyclerView.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("BangCongFragment", "API call failed or response body is null");
                    recyclerView.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SalaryTableResponse>> call, Throwable t) {
                loadingDialog.hide();
                Log.e("API Error", t.getMessage());
                // Hiển thị empty state
                recyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            }
        });
    }

}