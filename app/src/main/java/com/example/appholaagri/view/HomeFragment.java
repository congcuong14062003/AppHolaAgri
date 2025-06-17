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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.FunctionAdapter;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.FunctionItemHomeModel.FunctionItemHomeModel;
import com.example.appholaagri.model.MenuHomeModel.MenuHomeResponse;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.LoadingDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {
    private RecyclerView rvFarmManagement, rvTaskManagement, rvRequestProposal;
    private TextView userName, userInfo, tvFarmManagementTitle, tvTaskManagementTitle, tvRequestProposalTitle;
    private ImageView avtUser;
    private ConstraintLayout containerHome;
    private List<MenuHomeResponse.MenuItem> menuList; // Lưu trữ dữ liệu menu từ API
    private LoadingDialog loadingDialog;
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

        // Khai báo TextView cho các tiêu đề
        tvFarmManagementTitle = view.findViewById(R.id.tv_farm_management_title);
        tvTaskManagementTitle = view.findViewById(R.id.tv_task_management_title);
        tvRequestProposalTitle = view.findViewById(R.id.tv_request_proposal_title);


        loadingDialog = new LoadingDialog(getContext());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("HomeFragment", "token: " + token);

        if (token != null) {
            getUserData(token);
            getMenuData(token, "HOME");
        }

        return view;
    }

    private void setupFarmManagementRecyclerView(List<MenuHomeResponse.MenuItem> menuList) {
        List<FunctionItemHomeModel> farmItems = new ArrayList<>();

        // Tìm mục "Quản lý trang trại" trong menuList
        for (MenuHomeResponse.MenuItem menuItem : menuList) {
            if ("HOME_AGRI_FUNCTION".equals(menuItem.getCode())) {
                tvFarmManagementTitle.setText(menuItem.getTitle());
                List<MenuHomeResponse.ChildItem> childItems = menuItem.getChild();
                if (childItems != null) {
                    for (MenuHomeResponse.ChildItem child : childItems) {
                        farmItems.add(new FunctionItemHomeModel(
                                child.getTitle(),
                                child.getUrl(),
                                getActivityClass(child.getNextScreenCode())
                        ));
                    }
                }
                break;
            }
        }

        // Use LinearLayoutManager for horizontal scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFarmManagement.setLayoutManager(layoutManager);

        // Disable nested scrolling to prevent conflicts with ScrollView
        rvFarmManagement.setNestedScrollingEnabled(false);

        FunctionAdapter adapter = new FunctionAdapter(getContext(), farmItems, true);
        rvFarmManagement.setAdapter(adapter);

        // Adjust item width in the adapter based on item count
        if (farmItems.size() <= 3) {
            rvFarmManagement.setOverScrollMode(View.OVER_SCROLL_NEVER); // Disable scrolling for ≤3 items
        }
    }

    private void setupTaskManagementRecyclerView(List<MenuHomeResponse.MenuItem> menuList) {
        List<FunctionItemHomeModel> taskItems = new ArrayList<>();

        for (MenuHomeResponse.MenuItem menuItem : menuList) {
            if ("HOME_WORK_FUNCTION".equals(menuItem.getCode())) {
                tvTaskManagementTitle.setText(menuItem.getTitle());
                List<MenuHomeResponse.ChildItem> childItems = menuItem.getChild();
                if (childItems != null) {
                    for (MenuHomeResponse.ChildItem child : childItems) {
                        taskItems.add(new FunctionItemHomeModel(
                                child.getTitle(),
                                child.getUrl(),
                                getActivityClass(child.getNextScreenCode())
                        ));
                    }
                }
                break;
            }
        }

        // Use GridLayoutManager with spanCount=4 for 4 items per row
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvTaskManagement.setLayoutManager(layoutManager);
        rvTaskManagement.setNestedScrollingEnabled(false);

        FunctionAdapter adapter = new FunctionAdapter(getContext(), taskItems, false);
        rvTaskManagement.setAdapter(adapter);
    }

    private void setupRequestProposalRecyclerView(List<MenuHomeResponse.MenuItem> menuList) {
        List<FunctionItemHomeModel> requestItems = new ArrayList<>();

        for (MenuHomeResponse.MenuItem menuItem : menuList) {
            if ("HOME_REQUEST_FUNCTION".equals(menuItem.getCode())) {
                tvRequestProposalTitle.setText(menuItem.getTitle());
                List<MenuHomeResponse.ChildItem> childItems = menuItem.getChild();
                if (childItems != null) {
                    for (MenuHomeResponse.ChildItem child : childItems) {
                        requestItems.add(new FunctionItemHomeModel(
                                child.getTitle(),
                                child.getUrl(),
                                getActivityClass(child.getNextScreenCode()),
                                child.getId(),
                                child.getPosition()
                        ));
                    }
                }
                break;
            }
        }

        // Use GridLayoutManager with spanCount=4 for 4 items per row
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvRequestProposal.setLayoutManager(layoutManager);
        rvRequestProposal.setNestedScrollingEnabled(false);

        FunctionAdapter adapter = new FunctionAdapter(getContext(), requestItems, false);
        rvRequestProposal.setAdapter(adapter);
    }

    // Ánh xạ nextScreenCode với Activity tương ứng
    private Class<?> getActivityClass(String nextScreenCode) {
        switch (nextScreenCode) {
            case "PLANTATION_PLANT_LIST":
                return ListPlantationActivity.class;
            case "PLANT_EXAMINATION":
                return RecordConditionActivity.class;
            case "PLANT_SOIL_IDENTIFY":
                return DeclarationIdentifierActivity.class;
            case "CHECKIN_RECORD":
                return TimekeepingStatisticsActivity.class;
            case "MONTHLY_WORK_COEFFICIENT":
                return SalaryTableActivity.class;
            case "CAMPAIGN_MANAGE":
                return null; // Thay bằng Activity tương ứng nếu có
            case "WORK_SHIFT_MANAGE":
                return WorkShiftsActivity.class;
            case "CONFIRM_REPORT":
                return null; // Thay bằng Activity tương ứng nếu có
            case "WORK_REPORT":
                return null; // Thay bằng Activity tương ứng nếu có
            case "BUSINESS_TRIP_CREATE":
                return CreateRequestWorkReportActivity.class;
            case "LATE_EARLY_CREATE":
                return CreateRequestLateEarlyActivity.class;
            case "DAY_OFF_CREATE":
                return CreateRequestDayOffActivity.class;
            case "OVER_TIME_CREATE":
                return CreateRequestOvertTimeActivity.class;
            case "BUY_NEW_CREATE":
                return CreateRequestBuyNewActivity.class;
            case "RESIGN_CREATE":
                return CreateRequestResignActivity.class;
            default:
                return null;
        }
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

    private void getMenuData(String token, String screenCode) {
        loadingDialog.show();
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<ApiResponse<List<MenuHomeResponse.MenuItem>>> call = apiInterface.menuHome(token, screenCode);
        call.enqueue(new Callback<ApiResponse<List<MenuHomeResponse.MenuItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MenuHomeResponse.MenuItem>>> call, Response<ApiResponse<List<MenuHomeResponse.MenuItem>>> response) {
                loadingDialog.hide();
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<MenuHomeResponse.MenuItem>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        menuList = apiResponse.getData();
                        // Cập nhật các RecyclerView với dữ liệu từ API
                        setupFarmManagementRecyclerView(menuList);
                        setupTaskManagementRecyclerView(menuList);
                        setupRequestProposalRecyclerView(menuList);

                    } else {
                        CustomToast.showCustomToast(requireContext(), apiResponse.getMessage());
                    }
                } else {
                    Log.e("HomeFragment", "API response is unsuccessful");
                    CustomToast.showCustomToast(requireContext(), "Lỗi kết nối, vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MenuHomeResponse.MenuItem>>> call, Throwable t) {
                loadingDialog.hide();
                Log.e("HomeFragment", "Error: " + t.getMessage());
                if (isAdded()) {
                    CustomToast.showCustomToast(requireContext(), "Lỗi: " + t.getMessage());
                }
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
                    userInfo.setText(userData.getAccountDetail().getStaffCode() + " - " + userData.getWorkInfo().getDepartment().getName());
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