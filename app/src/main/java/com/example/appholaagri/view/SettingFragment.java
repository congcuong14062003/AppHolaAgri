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
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {

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
        } else {
            Toast.makeText(requireContext(), "Token không tồn tại", Toast.LENGTH_SHORT).show();
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
        try {
            // Create Retrofit and ApiInterface
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            // Call the API
            Call<ApiResponse<UserData>> call = apiInterface.userData(token);

            call.enqueue(new Callback<ApiResponse<UserData>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserData>> call, Response<ApiResponse<UserData>> response) {
                    Log.d("SettingFragment", "Response code: " + response.code());
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<UserData> apiResponse = response.body();
                        UserData userData = apiResponse.getData();
                        updateUserUI(userData);
                        contentLayout.setVisibility(View.VISIBLE); // Show layout after data is ready
                    } else {
                        Log.e("SettingFragment", "API response unsuccessful");
                        Toast.makeText(requireContext(), "Lỗi kết nối, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserData>> call, Throwable t) {
                    Log.e("SettingFragment", "Error: " + t.getMessage());
                    Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("SettingFragment", "Error during API call: " + e.getMessage());
            Toast.makeText(requireContext(), "Lỗi khi lấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(requireContext(), "Lỗi khi cập nhật giao diện.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogout() {
        try {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", requireActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("auth_token");
            editor.apply(); // Save changes
            Intent intent = new Intent(getActivity(), MainActivity.class); // Or LoginActivity
            startActivity(intent);
            requireActivity().finish(); // Close current activity
        } catch (Exception e) {
            Log.e("SettingFragment", "Error during logout: " + e.getMessage());
        }
    }
}
