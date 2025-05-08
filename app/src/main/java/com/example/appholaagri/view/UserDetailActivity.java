package com.example.appholaagri.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appholaagri.R;
import com.example.appholaagri.helper.UserDetailApiHelper;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.utils.CustomToast;
import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {
    private LinearLayout contentLayout;
    private TextView txtName, txtRole, txtPhone, txtEmail, txtBirthDay, txtIdentityCode, txtTaxCode, txtAddress, tv_don_vi_cong_tac_value, tv_phong_ban,tv_to_doi,tv_chuc_danh_value, tv_vi_tri_cong_viec_value, tv_quan_ly_truc_tiep_value, tv_loai_hop_dong_value, tv_ngay_bat_dau_lam_value, tv_ngay_lam_viec_chinh_thuc_value;
    private ImageView avatarUser, backBtnReview, cameraIcon;
    // Khai báo mã yêu cầu quyền
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        cameraIcon = findViewById(R.id.cameraIcon);
        contentLayout = findViewById(R.id.contentLayout);
        contentLayout.setVisibility(View.GONE);

        avatarUser = findViewById(R.id.avtUser);
        txtName = findViewById(R.id.txtName);
        txtRole = findViewById(R.id.txtRole);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtBirthDay = findViewById(R.id.txtBirthDay);
        txtIdentityCode = findViewById(R.id.txtIdentityCode);
        txtTaxCode = findViewById(R.id.txtTaxCode);
        txtAddress = findViewById(R.id.txtAddress);
        tv_don_vi_cong_tac_value = findViewById(R.id.tv_don_vi_cong_tac_value);
        tv_phong_ban = findViewById(R.id.tv_phong_ban);
        tv_to_doi = findViewById(R.id.tv_to_doi);
        tv_chuc_danh_value = findViewById(R.id.tv_chuc_danh_value);
        tv_vi_tri_cong_viec_value = findViewById(R.id.tv_vi_tri_cong_viec_value);
        tv_quan_ly_truc_tiep_value = findViewById(R.id.tv_quan_ly_truc_tiep_value);
        tv_loai_hop_dong_value = findViewById(R.id.tv_loai_hop_dong_value);
        tv_ngay_bat_dau_lam_value = findViewById(R.id.tv_ngay_bat_dau_lam_value);
        tv_ngay_lam_viec_chinh_thuc_value = findViewById(R.id.tv_ngay_lam_viec_chinh_thuc_value);
        backBtnReview = findViewById(R.id.backBtnReview);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Log.d("UserDetailActivity", "Token: " + token);

        if (token != null) {
            getUserData(token);
        }
        backBtnReview.setOnClickListener(view -> {
            finish();
        });
        cameraIcon.setOnClickListener(v -> checkCameraPermissionAndOpen());
    }
    @Override
    public void onBackPressed() {
        // Tìm ra HomeActivity và chuyển hướng về SettingFragment
        super.onBackPressed();
        Intent intent = new Intent(UserDetailActivity.this, HomeActivity.class);
        intent.putExtra("navigate_to", "setting"); // Thêm thông tin để xác định chuyển hướng đến SettingFragment
        startActivity(intent);
        finish();  // Kết thúc Activity này
    }

    private void getUserData(String token) {
        UserDetailApiHelper.getUserData(this, token, new UserDetailApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(UserData userData) {
                updateUserUI(userData);
                contentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserDetailActivity", "Error: " + errorMessage);
                CustomToast.showCustomToast(UserDetailActivity.this, "Lỗi: " + errorMessage);
            }
        });
    }

    private void updateUserUI(UserData userData) {
        try {
            if (userData == null) {
                return;
            }
            if (userData.getContractInfo() != null) {
                Picasso.get()
                        .load(userData.getUserAvatar())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(avatarUser);

                tv_loai_hop_dong_value.setText(userData.getContractInfo().getContract().getName());
                tv_ngay_bat_dau_lam_value.setText(userData.getContractInfo().getWorkStartDate());
                tv_ngay_lam_viec_chinh_thuc_value.setText(userData.getContractInfo().getOfficialWorkingDay());
            } else {
                avatarUser.setImageResource(R.drawable.avatar);
                tv_loai_hop_dong_value.setText("Thông tin hợp đồng không có sẵn");
                tv_ngay_bat_dau_lam_value.setText("Ngày bắt đầu làm việc không có sẵn");
                tv_ngay_lam_viec_chinh_thuc_value.setText("Ngày làm việc chính thức không có sẵn");
            }

            if (userData.getAccountDetail() != null) {
                txtName.setText(userData.getAccountDetail().getStaffName());
                txtRole.setText(userData.getAccountDetail().getStaffCode() + " - " + userData.getWorkInfo().getTitle().getName());
                txtPhone.setText(userData.getAccountDetail().getUsername());
                txtEmail.setText(userData.getAccountDetail().getEmail());
            } else {
                txtName.setText("Tên không có sẵn");
                txtRole.setText("Mã nhân viên không có sẵn");
                txtPhone.setText("Số điện thoại không có sẵn");
                txtEmail.setText("Email không có sẵn");
            }

            if (userData.getUserInfo() != null) {
                txtBirthDay.setText(userData.getUserInfo().getBirthDay());
                txtIdentityCode.setText(userData.getUserInfo().getIdentityCode());
                txtTaxCode.setText(userData.getUserInfo().getTaxCode());
                txtAddress.setText(userData.getUserInfo().getAddress());
            } else {
                txtBirthDay.setText("Ngày sinh không có sẵn");
                txtIdentityCode.setText("Mã số căn cước không có sẵn");
                txtTaxCode.setText("Mã số thuế không có sẵn");
                txtAddress.setText("Địa chỉ không có sẵn");
            }

            if (userData.getWorkInfo() != null) {
                tv_don_vi_cong_tac_value.setText(userData.getWorkInfo().getUnit().getName());
                tv_phong_ban.setText(userData.getWorkInfo().getDepartment().getName());
                tv_to_doi.setText(userData.getWorkInfo().getTeam().getName());
                tv_chuc_danh_value.setText(userData.getWorkInfo().getTitle().getName());
                tv_vi_tri_cong_viec_value.setText(userData.getWorkInfo().getPosition().getName());
                tv_quan_ly_truc_tiep_value.setText(userData.getWorkInfo().getManager().getName());
            } else {
                tv_don_vi_cong_tac_value.setText("Đơn vị công tác không có sẵn");
                tv_phong_ban.setText("Phòng ban không có sẵn");
                tv_to_doi.setText("Tổ đội không có sẵn");
                tv_chuc_danh_value.setText("Chức danh không có sẵn");
                tv_vi_tri_cong_viec_value.setText("Vị trí công việc không có sẵn");
                tv_quan_ly_truc_tiep_value.setText("Quản lý trực tiếp không có sẵn");
            }

            contentLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage());
        }
    }
    private void checkCameraPermissionAndOpen() {
        // Kiểm tra nếu ứng dụng đã có quyền truy cập vào camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Nếu có quyền, mở camera
            openCamera();
        } else {
            // Nếu chưa có quyền, yêu cầu quyền
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }
    }
    // Xử lý kết quả khi người dùng cấp quyền hoặc từ chối
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Nếu quyền được cấp, mở camera
                    openCamera();
                } else {
                    // Nếu quyền bị từ chối, mở thư viện ảnh
                    openGallery();
                }
                break;
        }
    }

    // Mở camera
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả từ camera hoặc thư viện ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(avatarUser); // Đặt ảnh người dùng chọn vào avatar
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            // Nhận kết quả từ camera và cập nhật avatar
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(avatarUser); // Đặt ảnh camera vào avatar
        }
    }
}