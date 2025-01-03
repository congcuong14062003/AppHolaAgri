package com.example.appholaagri.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appholaagri.R;
import com.example.appholaagri.model.UserData.UserData;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserDetailActivity extends AppCompatActivity {
    private LinearLayout contentLayout;
    private TextView txtName, txtRole, txtPhone, txtEmail, txtBirthDay, txtIdentityCode, txtTaxCode, txtAddress, tv_don_vi_cong_tac_value, tv_phong_ban,tv_to_doi,tv_chuc_danh_value, tv_vi_tri_cong_viec_value, tv_quan_ly_truc_tiep_value, tv_loai_hop_dong_value, tv_ngay_bat_dau_lam_value, tv_ngay_lam_viec_chinh_thuc_value;
    private ImageView avatarUser, backBtnReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Initialize UI elements
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
        } else {
            Toast.makeText(this, "Token không tồn tại", Toast.LENGTH_SHORT).show();
        }
        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });

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
        try {
            ApiInterface apiInterface = ApiClient.getClient(getBaseContext()).create(ApiInterface.class);
            Call<ApiResponse<UserData>> call = apiInterface.userData(token);
            call.enqueue(new Callback<ApiResponse<UserData>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserData>> call, Response<ApiResponse<UserData>> response) {
                    try {
                        Log.d("UserDetailActivity", "Response code: " + response.code());
                        Log.d("UserDetailActivity", "Response message: " + response.message());

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<UserData> apiResponse = response.body();
                            Log.d("UserDetailActivity", "API response body: " + apiResponse.toString());
                            UserData userData = apiResponse.getData();
                            updateUserUI(userData);
                            contentLayout.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("UserDetailActivity", "API response is unsuccessful");
                            Toast.makeText(UserDetailActivity.this, "Lỗi kết nối, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("UserDetailActivity", "Error during response handling: " + e.getMessage());
                        Toast.makeText(UserDetailActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserData>> call, Throwable t) {
                    Log.e("UserDetailActivity", "Error: " + t.getMessage());
                    Toast.makeText(UserDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error during API call: " + e.getMessage());
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserUI(UserData userData) {
        try {
            if (userData == null) {
                return;
            }

            if (userData.getContractInfo() != null) {
                Picasso.get()
                        .load(userData.getContractInfo().getUrlFile())
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
            Toast.makeText(this, "Có lỗi xảy ra khi cập nhật thông tin người dùng.", Toast.LENGTH_SHORT).show();
        }
    }
}
