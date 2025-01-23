package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appholaagri.R;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.service.NetworkUtil;
import com.example.appholaagri.utils.CustomToast;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends BaseFragment {

    private ImageView avtUser;
    private TextView txtStaffName, txtStaffCode;
    private LinearLayout contentLayout, btnManageInfo, btnSetting, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize Views
        contentLayout = view.findViewById(R.id.contentLayout);
        avtUser = view.findViewById(R.id.avtUser);
        txtStaffName = view.findViewById(R.id.txtStaffName);
        txtStaffCode = view.findViewById(R.id.txtStaffCode);
        contentLayout.setVisibility(View.GONE); // Hide layout initially

        // Find Manage Info button
        btnManageInfo = view.findViewById(R.id.btnManageInfo);
        // Find Logout button
        btnLogout = view.findViewById(R.id.btnLogout);
        // Find setting security
        btnSetting = view.findViewById(R.id.btnSetting);
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null) {
            getUserData(token);
        }

        // Handle Manage Info button click
        btnManageInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            startActivity(intent);
        });

        // Handle Logout button click
        btnLogout.setOnClickListener(v -> handleLogout());
        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingSecurityActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void getUserData(String token) {
        UserDetailApiHelper.getUserData(getContext(), token, new UserDetailApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(UserData userData) {
                updateUserUI(userData);
                contentLayout.setVisibility(View.VISIBLE); // Hiển thị layout sau khi dữ liệu sẵn sàng
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("SettingFragment", "Error: " + errorMessage);
                CustomToast.showCustomToast(requireContext(), "Lỗi: " + errorMessage);
            }
        });
    }


    private void updateUserUI(UserData userData) {
        try {
            if (userData == null) {
                return;
            }
            // Check for ContractInfo before using it
            if (userData.getContractInfo() != null) {
                Picasso.get()
                        .load(userData.getUserAvatar()) // URL of image
                        .placeholder(R.drawable.avatar) // Placeholder image while loading
                        .error(R.drawable.avatar) // Error image if loading fails
                        .into(avtUser); // Set the image to ImageView
            } else {
                avtUser.setImageResource(R.drawable.avatar); // Set default image if contractInfo is null
            }
            // Check AccountDetail and WorkInfo for null before using them
            if (userData.getAccountDetail() != null) {
                txtStaffName.setText(userData.getAccountDetail().getStaffName());
                if (userData.getWorkInfo() != null && userData.getWorkInfo().getTitle() != null) {
                    txtStaffCode.setText(userData.getAccountDetail().getStaffCode() + " - " + userData.getWorkInfo().getTitle().getName());
                } else {
                    txtStaffCode.setText("Thông tin công việc không có sẵn");
                }
            } else {
                txtStaffName.setText("Tên không có sẵn");
                txtStaffCode.setText("Mã nhân viên không có sẵn");
            }
            contentLayout.setVisibility(View.VISIBLE); // Show layout after data is updated
        } catch (Exception e) {
            CustomToast.showCustomToast(requireContext(),  "Lỗi khi cập nhật giao diện.");
        }
    }

    private void handleLogout() {
        try {
            // Lấy SharedPreferences và xóa token
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("auth_token");
            editor.apply(); // Lưu thay đổi

            // Khởi tạo Intent và chuyển hướng đến MainActivity (hoặc LoginActivity)
            Intent intent = new Intent(getActivity(), MainActivity.class); // Hoặc LoginActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Đảm bảo không quay lại màn hình trước đó

            startActivity(intent);
            requireActivity().finish(); // Đóng Activity hiện tại

        } catch (Exception e) {
            Log.e("SettingFragment", "Error during logout: " + e.getMessage());
        }
    }

}
