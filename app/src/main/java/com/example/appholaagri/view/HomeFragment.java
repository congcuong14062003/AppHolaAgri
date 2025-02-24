package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appholaagri.R;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.utils.CustomToast;
import com.squareup.picasso.Picasso;

public class HomeFragment extends BaseFragment {
    private LinearLayout thongkechamcong_container, bangcongluong_container, yeucau_dexuat_container, dinhdanh_container;
    private TextView userName, userInfo;
    private ImageView avtUser;
    private ConstraintLayout container_home;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        thongkechamcong_container = view.findViewById(R.id.thongkechamcong_container);
        bangcongluong_container = view.findViewById(R.id.bangcongluong_container);
        yeucau_dexuat_container = view.findViewById(R.id.yeucau_dexuat_container);
        dinhdanh_container = view.findViewById(R.id.dinhdanh_container);
        userName = view.findViewById(R.id.user_name);
        userInfo = view.findViewById(R.id.user_info);
        avtUser = view.findViewById(R.id.avtUser);
        container_home = view.findViewById(R.id.container_home);

        container_home.setVisibility(View.GONE); // Hide layout initially
        // sự kiện click
        thongkechamcong_container.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), TimekeepingStatisticsActivity.class);
            startActivity(intent);
        });
        bangcongluong_container.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), SalaryTableActivity.class);
            startActivity(intent);
        });
        yeucau_dexuat_container.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), RequestActivity.class);
            startActivity(intent);
        });

        dinhdanh_container.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), DeclarationIdentifierActivity.class);
            startActivity(intent);
        });
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("HomeFragment", "token: " + token);

        if (token != null) {
            getUserData(token);
        }
        return view;
    }
    private void getUserData(String token) {
        UserDetailApiHelper.getUserData(getContext(), token, new UserDetailApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(UserData userData) {
                updateUserUI(userData);
                container_home.setVisibility(View.VISIBLE); // Hiển thị layout sau khi dữ liệu sẵn sàng
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
            container_home.setVisibility(View.VISIBLE); // Show layout after data is updated
        } catch (Exception e) {
            CustomToast.showCustomToast(requireContext(),  "Lỗi khi cập nhật giao diện.");
        }
    }
}