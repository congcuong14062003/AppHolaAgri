package com.example.appholaagri.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.appholaagri.R;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.utils.CustomToast;

public class PlatationDetailMapFragment extends Fragment {

    private WebView webViewMap;
    private int plantationId;
    private String token;
    private String username;
    private ImageView backBtnReview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_platation_detail_map, container, false);

        // Khởi tạo WebView
        webViewMap = view.findViewById(R.id.webViewMap);
        backBtnReview = view.findViewById(R.id.backBtnReview);

        // Xử lý sự kiện click cho nút back
        backBtnReview.setOnClickListener(v -> {
            // Gọi finish() để quay lại màn trước
            requireActivity().finish();
        });
        // Lấy plantationId từ Bundle
        if (getArguments() != null) {
            plantationId = getArguments().getInt("plantationId", -1);
        }

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("auth_token", null);

        // Cấu hình WebView
        WebSettings webSettings = webViewMap.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu bản đồ yêu cầu
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // Lấy username từ API và load WebView
        if (token != null) {
            fetchUsernameAndLoadMap();
        } else {
            Toast.makeText(requireContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchUsernameAndLoadMap() {
        UserDetailApiHelper.getUserData(requireContext(), token, new UserDetailApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(UserData userData) {
                username = userData.getAccountDetail().getUsername(); // Giả sử UserData có phương thức getUsername()
                loadMapUrl();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Lỗi khi lấy username: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMapUrl() {
        // Xác định version dựa trên plantationId
        String version = (plantationId == 18) ? "V2" : (plantationId == 19) ? "V1" : null;

        if (version != null && username != null) {
            // Xây dựng URL
            String mapUrl = String.format("https://aom.imediatech.com.vn/ban-do/%s?token=%s&username=%s",
                    version, token, username);
            // Load URL vào WebView
            webViewMap.loadUrl(mapUrl);
        } else {
            CustomToast.showCustomToast(getContext(), "Không thể tải bản đồ: Dữ liệu không hợp lệ");
        }
    }
}