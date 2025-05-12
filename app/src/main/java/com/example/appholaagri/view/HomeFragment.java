package com.example.appholaagri.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.FunctionAdapter;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.FunctionItemHomeModel.FunctionItemHomeModel;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.utils.CustomToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private RecyclerView rvFarmManagement, rvTaskManagement, rvRequestProposal;
    private TextView userName, userInfo;
    private ImageView avtUser;
    private ConstraintLayout containerHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvFarmManagement = view.findViewById(R.id.rv_farm_management);
        rvTaskManagement = view.findViewById(R.id.rv_task_management);
        rvRequestProposal = view.findViewById(R.id.rv_request_proposal);

        userName = view.findViewById(R.id.user_name);
        userInfo = view.findViewById(R.id.user_info);
        avtUser = view.findViewById(R.id.avtUser);
        containerHome = view.findViewById(R.id.container_home);

        setupFarmManagementRecyclerView();
        setupTaskManagementRecyclerView();
        setupRequestProposalRecyclerView();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("HomeFragment", "token: " + token);

        if (token != null) {
            getUserData(token);
        }

        return view;
    }

    private void setupFarmManagementRecyclerView() {
        List<FunctionItemHomeModel> farmItems = new ArrayList<>();
        farmItems.add(new FunctionItemHomeModel("Quản lý cây trồng", R.drawable.qlct, ListPlantationActivity.class));
        farmItems.add(new FunctionItemHomeModel("Khai báo định danh", R.drawable.ddqt, DeclarationIdentifierActivity.class));
        farmItems.add(new FunctionItemHomeModel("Ghi nhận tình trạng", R.drawable.gntt, RecordConditionActivity.class));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvFarmManagement.setLayoutManager(layoutManager);
        FunctionAdapter adapter = new FunctionAdapter(getContext(), farmItems, true); // true cho item_farm
        rvFarmManagement.setAdapter(adapter);
    }

    private void setupTaskManagementRecyclerView() {
        List<FunctionItemHomeModel> taskItems = new ArrayList<>();
        taskItems.add(new FunctionItemHomeModel("Thống kê chấm công", R.drawable.tkcc, TimekeepingStatisticsActivity.class));
        taskItems.add(new FunctionItemHomeModel("Chiến dịch điều động", R.drawable.cddd, null));
        taskItems.add(new FunctionItemHomeModel("Xác nhận báo cáo", R.drawable.tkcc, null));
        taskItems.add(new FunctionItemHomeModel("Báo cáo công việc", R.drawable.tkcc, null));
        taskItems.add(new FunctionItemHomeModel("Bảng tính công", R.drawable.btc, SalaryTableActivity.class));
        taskItems.add(new FunctionItemHomeModel("Đo thủ công", R.drawable.dtc, ManualMeasurementActivity.class));
        taskItems.add(new FunctionItemHomeModel("Phân ca làm việc", R.drawable.pclv, WorkShiftsActivity.class));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rvTaskManagement.setLayoutManager(layoutManager);
        FunctionAdapter adapter = new FunctionAdapter(getContext(), taskItems, false); // false cho item_home
        rvTaskManagement.setAdapter(adapter);
    }

    private void setupRequestProposalRecyclerView() {
        List<FunctionItemHomeModel> requestItems = new ArrayList<>();
        requestItems.add(new FunctionItemHomeModel("Đi muộn về sớm", R.drawable.dmvs, CreateRequestLateEarlyActivity.class, 1, 1));
        requestItems.add(new FunctionItemHomeModel("Đơn xin nghỉ phép", R.drawable.dxnp, CreateRequestDayOffActivity.class, 2, 2));
        requestItems.add(new FunctionItemHomeModel("Đăng ký làm thêm", R.drawable.dklt, CreateRequestOvertTimeActivity.class, 3, 3));
        requestItems.add(new FunctionItemHomeModel("Mua sắm vật tư", R.drawable.msvt, CreateRequestBuyNewActivity.class, 4, 4));
        requestItems.add(new FunctionItemHomeModel("Đơn xin thôi việc", R.drawable.dxtv, CreateRequestResignActivity.class, 5, 5));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvRequestProposal.setLayoutManager(layoutManager);
        FunctionAdapter adapter = new FunctionAdapter(getContext(), requestItems, false); // false cho item_home
        rvRequestProposal.setAdapter(adapter);
    }

    private void getUserData(String token) {
        UserDetailApiHelper.getUserData(getContext(), token, new UserDetailApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(UserData userData) {
                updateUserUI(userData);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("HomeFragment", "Error: " + errorMessage);
                CustomToast.showCustomToast(requireContext(), "Lỗi: " + errorMessage);
            }
        });
    }

    private void updateUserUI(UserData userData) {
        try {
            if (userData == null) return;
            if (userData.getContractInfo() != null) {
                Picasso.get().load(userData.getUserAvatar()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(avtUser);
            } else {
                avtUser.setImageResource(R.drawable.avatar);
            }
            if (userData.getAccountDetail() != null) {
                userName.setText(userData.getAccountDetail().getStaffName());
                if (userData.getWorkInfo() != null && userData.getWorkInfo().getTitle() != null) {
                    userInfo.setText(userData.getAccountDetail().getStaffCode() + " - " + userData.getWorkInfo().getTitle().getName());
                } else {
                    userInfo.setText("Thông tin công việc không có sẵn");
                }
            } else {
                userName.setText("Tên không có sẵn");
                userInfo.setText("Mã nhân viên không có sẵn");
            }
            containerHome.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            CustomToast.showCustomToast(requireContext(), "Lỗi khi cập nhật giao diện.");
        }
    }
}