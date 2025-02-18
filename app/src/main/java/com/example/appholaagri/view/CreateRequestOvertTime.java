package com.example.appholaagri.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.DayOverTimeAdapter;
import com.example.appholaagri.adapter.RequestMethodAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.BreakTime;
import com.example.appholaagri.model.RequestDetailModel.Consignee;
import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.example.appholaagri.model.RequestDetailModel.ListDayReq;
import com.example.appholaagri.model.RequestDetailModel.ListStatus;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.RequestDetailModel.RequestMethod;
import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestOvertTime extends AppCompatActivity {
    private EditText edt_name_request_create, edt_name_employye_request_create, edt_part_request_create, etNgayBatDau,etGioBatDau, etNgayKetThuc,etGioKetThuc,
            edt_reason_request_create, edt_manager_direct_request_create, edt_fixed_reviewer_request_create, edt_follower_request_create;
    private TextView title_request, txt_type_request_create, select_method_request;
    private ImageView backBtnReview;
    private RequestDetailData requestDetailData; // Biến toàn cục
    private Integer GroupRequestType, GroupRequestId, requestId, StatusRequest, selectedMinute = 30;
    private LinearLayout breakTimeContainer,dayContainer;
    // over lay
    View overlay_background;
    private ConstraintLayout overlay_filter_status_container;
    ConstraintLayout overlayFilterStatus;

    // Danh sách lưu thời gian nghỉ giữa giờ
    private List<BreakTime> breakTimes = new ArrayList<>();
    private List<View> dayViewList = new ArrayList<>();
    private Button addDayBtn;
    private List<ListDayReq> listDayReqs = new ArrayList<>();
    private RecyclerView dayRecyclerView;
    private DayOverTimeAdapter dayOverTimeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request_overt_time);

        // ánh xạ
        // nút back
        backBtnReview = findViewById(R.id.backBtnReview_create);
        // tiêu đề
        title_request = findViewById(R.id.title_request);
        // loại đề xuất
        txt_type_request_create = findViewById(R.id.txt_type_request_create);
        // tên đề xuất
        edt_name_request_create = findViewById(R.id.edt_name_request_create);
        // người đề xuất
        edt_name_employye_request_create = findViewById(R.id.edt_name_employye_request_create);
        // bộ phận
        edt_part_request_create = findViewById(R.id.edt_part_request_create);
        // sự kiện cho làm thêm

        addDayBtn = findViewById(R.id.add_day_btn);

        // lí do
        edt_reason_request_create = findViewById(R.id.edt_reason_request_create);
        // quản ly trực tiếp
        edt_manager_direct_request_create = findViewById(R.id.edt_manager_direct_request_create);
        // người duyệt cố định
        edt_fixed_reviewer_request_create = findViewById(R.id.edt_fixed_reviewer_request_create);
        // người theo dõi
        edt_follower_request_create = findViewById(R.id.edt_follower_request_create);
        // over lay
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);
        overlay_background = findViewById(R.id.overlay_background);
        overlay_filter_status_container = findViewById(R.id.overlay_filter_status_container);

        // recycle view
        dayRecyclerView = findViewById(R.id.dayRecyclerView);



        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        Intent intent = getIntent();
        if (intent != null) {
            GroupRequestId = intent.getIntExtra("GroupRequestId", -1); // Nhận requestId
            GroupRequestType = intent.getIntExtra("GroupRequestType", -1); // Nhận requestId
            StatusRequest = intent.getIntExtra("StatusRequest", -1);
            requestId = intent.getIntExtra("requestId", -1);
            Log.d("CreateRequestOvertTime", "StatusRequest: " + StatusRequest);
        }

        if(StatusRequest > 2){
            edt_name_request_create.setEnabled(false);
            edt_name_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            select_method_request.setEnabled(false);
            select_method_request.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));
            edt_reason_request_create.setEnabled(false);
            edt_reason_request_create.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee0df")));

        }
        // init
        dayOverTimeAdapter = new DayOverTimeAdapter(listDayReqs, this, this);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayRecyclerView.setAdapter(dayOverTimeAdapter);

        addNewDay();


        // Khởi tạo nếu null
        if (requestDetailData == null) {
            requestDetailData = new RequestDetailData();
            requestDetailData.setDuration(30);
        }

        if (requestId != -1) {
            title_request.setText("Chi tiết đề xuất");
            getDetailRequest(requestId, token);
        } else {
            if (GroupRequestId != null && token != null) {
                Log.d("CreateRequestOvertTime", "Vào");
                getInitFormCreateRequest(token, GroupRequestId);
            }
        }


        // event
        Button buttonCloseOverlay = findViewById(R.id.button_close_overlay);
        overlayFilterStatus = findViewById(R.id.overlay_filter_status);

        backBtnReview.setOnClickListener(view -> {
            onBackPressed();
        });

        buttonCloseOverlay.setOnClickListener(v -> {
            overlayFilterStatus.setVisibility(View.GONE);
            overlay_background.setVisibility(View.GONE);
        });


        overlay_background.setOnTouchListener((view, event) -> {
            // Check if the touch event is outside the overlay_filter_status_container
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Get the coordinates of the touch
                float x = event.getX();
                float y = event.getY();

                // Check if the touch is outside the overlay_filter_status_container
                int[] location = new int[2];
                overlay_filter_status_container.getLocationOnScreen(location);
                int containerX = location[0];
                int containerY = location[1];
                int containerWidth = overlay_filter_status_container.getWidth();
                int containerHeight = overlay_filter_status_container.getHeight();

                if (x < containerX || x > containerX + containerWidth || y < containerY || y > containerY + containerHeight) {
                    // Touch is outside the container, so hide the overlay
                    overlayFilterStatus.setVisibility(View.GONE);
                    overlay_background.setVisibility(View.GONE);
                }
            }
            return true; // Handle the touch event and prevent further propagation
        });


        addDayBtn.setOnClickListener(v -> {
            addNewDay();
        });

    }
    private void addNewDay() {
        // Thêm một đối tượng mới vào mảng listDayReqs
        listDayReqs.add(new ListDayReq(new ArrayList<>(), "", "", " "));
        // Cập nhật lại adapter của DayOverTimeAdapter
        // Cập nhật lại adapter của breakTimeRecycler
        dayOverTimeAdapter.notifyItemInserted(listDayReqs.size() - 1);
        // Cập nhật lại toàn bộ adapter của Day
        dayOverTimeAdapter.notifyDataSetChanged();  // Đảm bảo cập nhật lại danh sách toàn bộ
    }



    // Cập nhật RequestDetailData với danh sách listDayReqs
    public void updateRequestDetailData() {
        // Cập nhật mảng listDayReqs vào requestDetailData
        requestDetailData.setListDayReqs(listDayReqs);

        // Chuyển dữ liệu sang JSON để kiểm tra
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String dataObjects = gson.toJson(requestDetailData);
        Log.d("cuong", "Dữ liệu listDayReqs hiện tại: " + dataObjects);
    }



    // init data
    private void getInitFormCreateRequest(String token, int GroupRequestId) {
        // Gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.initCreateRequest(token, GroupRequestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("CreateRequestOvertTime", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("CreateRequestOvertTime", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestOvertTime.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }
    // nếu có id lấy data chi tiết của nháp và từ chối
    private void getDetailRequest(int requestId, String token) {
        // Gọi API
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<RequestDetailData>> call = apiInterface.requestDetailData(token, requestId);
        call.enqueue(new Callback<ApiResponse<RequestDetailData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RequestDetailData>> call, Response<ApiResponse<RequestDetailData>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<RequestDetailData> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            requestDetailData = apiResponse.getData();
                            updateUserUI(requestDetailData);
                        }
                    } else {
                        Log.e("RequestDetailActivity", "API response is unsuccessful");
                        CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    Log.e("RequestDetailActivity", "Error during response handling: " + e.getMessage());
                    CustomToast.showCustomToast(CreateRequestOvertTime.this, "Có lỗi xảy ra. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RequestDetailData>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("ApiHelper", errorMessage);
            }
        });
    }
    // cập nhật giao diện
    private void updateUserUI(RequestDetailData requestDetailData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String requestDetailDataJson = gson.toJson(requestDetailData);
        Log.d("CreateRequestOvertTime", "requestDetailDataJson: " + requestDetailDataJson);

        try {
            if (requestDetailData == null) {
                Log.e("CreateRequestOvertTime", "requestDetailData is null");
                return;
            }

            // Kiểm tra từng thuộc tính trước khi gọi
            if (requestDetailData.getRequestGroup() != null && requestDetailData.getRequestGroup().getName() != null) {
                txt_type_request_create.setText(requestDetailData.getRequestGroup().getName());
            }

            if (requestDetailData.getRequestName() != null) {
                edt_name_request_create.setText(requestDetailData.getRequestName());
            }

            if (requestDetailData.getEmployee() != null && requestDetailData.getEmployee().getName() != null) {
                edt_name_employye_request_create.setText(requestDetailData.getEmployee().getName());
            }

            if (requestDetailData.getDepartment() != null && requestDetailData.getDepartment().getName() != null) {
                edt_part_request_create.setText(requestDetailData.getDepartment().getName());
            }

//            // Lấy danh sách listDayReqs từ requestDetailData
        // Giả sử requestDetailData.getListDayReqs() trả về một danh sách

//            List<ListDayReq> listDayReqs = requestDetailData.getListDayReqs();
//
//        // Chuyển đổi listDayReqs thành chuỗi JSON (nếu cần thiết)
//            String datalistDayReqs = gson.toJson(listDayReqs);
//            Log.d("datalistDayReqs", "datalistDayReqs: " + datalistDayReqs);
//
//        // Cập nhật lại danh sách trong adapter
//            if (dayOverTimeAdapter != null) {
//                dayOverTimeAdapter.updateData(listDayReqs);  // Giả sử có phương thức updateData trong Adapter
//            } else {
//                // Nếu adapter chưa được khởi tạo, tạo mới và gán vào RecyclerView
//                dayOverTimeAdapter = new DayOverTimeAdapter(listDayReqs, this, this);
//                dayRecyclerView.setAdapter(dayOverTimeAdapter);  // Giả sử recyclerView là đối tượng RecyclerView của bạn
//            }

//
//            // Kiểm tra xem danh sách có null không, nếu có thì khởi tạo danh sách rỗng
//            if (listDayReqs == null) {
//                listDayReqs = new ArrayList<>();
//            }
//
//            // Khởi tạo adapter và gán dữ liệu
//            dayOverTimeAdapter = new DayOverTimeAdapter(listDayReqs, this, this);
//            dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            dayRecyclerView.setAdapter(dayOverTimeAdapter);
//
//            // Thông báo adapter cập nhật dữ liệu
//            dayOverTimeAdapter.notifyDataSetChanged();

            if (requestDetailData.getReason() != null) {
                edt_reason_request_create.setText(requestDetailData.getReason());
            }

            if (requestDetailData.getDirectManager() != null
                    && requestDetailData.getDirectManager().getName() != null) {
                edt_manager_direct_request_create.setText(requestDetailData.getDirectManager().getName());
            } else {
                edt_manager_direct_request_create.setText("Chưa có quản lý trực tiếp");
            }

            // Xử lý danh sách Consignee
            List<Consignee> consigneeList = requestDetailData.getConsignee();
            if (consigneeList != null && !consigneeList.isEmpty()) {
                StringBuilder consigneeNames = new StringBuilder();
                for (Consignee consignee : consigneeList) {
                    if (consignee != null && consignee.getName() != null) {
                        consigneeNames.append(consignee.getName()).append(" ");
                    }
                }
                edt_fixed_reviewer_request_create.setText(consigneeNames.toString().trim());
            } else {
                edt_fixed_reviewer_request_create.setText("Không có người duyệt cố định");
            }

            // Xử lý danh sách Follower
            List<Follower> followerList = requestDetailData.getFollower();
            if (followerList != null && !followerList.isEmpty()) {
                StringBuilder followerNames = new StringBuilder();
                for (Follower follower : followerList) {
                    if (follower != null && follower.getName() != null) {
                        followerNames.append(follower.getName()).append(" ");
                    }
                }
                edt_follower_request_create.setText(followerNames.toString().trim());
            } else {
                edt_follower_request_create.setText("Không có người theo dõi");
            }

            // Xử lý danh sách RequestMethod
            List<RequestMethod> listMethods = requestDetailData.getListMethod();
            if (listMethods != null && !listMethods.isEmpty()) {
                RecyclerView recyclerView = findViewById(R.id.recycler_filter_status_create);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                RequestMethodAdapter adapter = new RequestMethodAdapter(
                        listMethods,
                        select_method_request,
                        overlayFilterStatus,
                        overlay_background,
                        GroupRequestType,
                        selectedMethod -> {
                            requestDetailData.setRequestMethod(selectedMethod);
                        }
                );
                recyclerView.setAdapter(adapter);

                // Nếu có requestId, đặt selectedItem từ API
                if (requestId != -1 && requestDetailData.getRequestMethod() != null) {
                    int selectedId = requestDetailData.getRequestMethod().getId();
                    adapter.setSelectedMethodById(selectedId);
                    recyclerView.scrollToPosition(
                            IntStream.range(0, listMethods.size())
                                    .filter(i -> listMethods.get(i).getId() == selectedId)
                                    .findFirst()
                                    .orElse(0)
                    );
                }
            } else {
                Log.e("CreateRequestActivity", "listMethods is null or empty");
            }

            // Xử lý danh sách ListStatus
            List<ListStatus> listStatus = requestDetailData.getListStatus();
            LinearLayout actionButtonContainer = findViewById(R.id.action_button_container);
            actionButtonContainer.removeAllViews();
            if (listStatus != null && !listStatus.isEmpty()) {
                for (ListStatus status : listStatus) {
                    if (status != null && status.getName() != null) {
                        AppCompatButton button = new AppCompatButton(this);
                        button.setText(status.getName());
                        button.setTextColor(Color.WHITE);
                        button.setPadding(24, 12, 24, 12);

                        String color = (status.getColor() != null) ? status.getColor() : "#007BFF";
                        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_custom));
                        button.setStateListAnimator(null);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                        params.setMargins(4, 8, 4, 8);
                        button.setLayoutParams(params);

                        button.setOnClickListener(v ->
                                {
//                                    if(requestId == -1) {
                                    handleCreateRequest(requestDetailData, status);
//                                    }
                                }
                        );
                        actionButtonContainer.addView(button);
                    }
                }
            } else {
                Log.e("ButtonList", "listStatus is null or empty");
            }

        } catch (Exception e) {
            Log.e("UserDetailActivity", "Error updating UI: " + e.getMessage(), e);
        }
    }
    // tạo request
    public void handleCreateRequest(RequestDetailData requestDetailData, ListStatus listStatus1) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        // Lấy dữ liệu từ giao diện nhập vào requestDetailData
        requestDetailData.setRequestName(edt_name_request_create.getText().toString().trim());
        requestDetailData.setReason(edt_reason_request_create.getText().toString().trim());
        // validate
        if (requestDetailData.getRequestName().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập tên đề xuất!");
            return;
        }
        if (requestDetailData.getReason().isEmpty()) {
            CustomToast.showCustomToast(this, "Vui lòng nhập lý do!");
            return;
        }


        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // Tạo object để gửi request
        GroupRequestCreateRequest groupRequestCreateRequest = new GroupRequestCreateRequest();
        groupRequestCreateRequest.setRequestName(requestDetailData.getRequestName());
        groupRequestCreateRequest.setReason(requestDetailData.getReason());
        // Gán thời gian đăng ký làm thêm
        List<ListDayReq> listDayReqs = requestDetailData.getListDayReqs();
        // Dùng Stream để chuyển đổi và gán vào listDayReqs của GroupRequestCreateRequest
        List<GroupRequestCreateRequest.ListDayReq> groupListDayReqs = listDayReqs.stream()
                .map(listDayReq -> new GroupRequestCreateRequest.ListDayReq(
                        listDayReq.getBreakTimes().stream()
                                .map(breakTime -> new GroupRequestCreateRequest.BreakTime(breakTime.getStartTime(), breakTime.getEndTime()))
                                .collect(Collectors.toList()),
                        listDayReq.getDay(),
                        listDayReq.getStartTime(),
                        listDayReq.getEndTime()))
                .collect(Collectors.toList());

        // Gán danh sách đã chuyển đổi vào groupRequestCreateRequest
        groupRequestCreateRequest.setListDayReqs(groupListDayReqs);


        // Gán requestGroup
        GroupRequestCreateRequest.RequestGroup requestGroup = new GroupRequestCreateRequest.RequestGroup(
                requestDetailData.getRequestGroup().getCode(),
                requestDetailData.getRequestGroup().getId(),
                requestDetailData.getRequestGroup().getName(),
                requestDetailData.getRequestGroup().getStatus()
        );
        groupRequestCreateRequest.setRequestGroup(requestGroup);
        // Gán status
        GroupRequestCreateRequest.Status status = new GroupRequestCreateRequest.Status(
                listStatus1.getCode(),
                listStatus1.getId(),
                listStatus1.getName(),
                listStatus1.getStatus()
        );
        groupRequestCreateRequest.setStatus(status);

        groupRequestCreateRequest.setRequestId(requestDetailData.getRequestId());
        groupRequestCreateRequest.setRequestName(requestDetailData.getRequestName());
        groupRequestCreateRequest.setType(requestDetailData.getType());
        // Tạo JSON log để kiểm tra dữ liệu

        if(requestId == -1) {
            String dataObjects = gson.toJson(groupRequestCreateRequest);
            Log.d("CreateRequestOvertTime", "Data thêm mới: " + dataObjects);
            Call<ApiResponse<String>> call = apiInterface.overTimeCreateRequest(token, groupRequestCreateRequest);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestOvertTime.this, apiResponse.getMessage());
                            startActivity(new Intent(CreateRequestOvertTime.this, RequestActivity.class));
                        } else {
                            CustomToast.showCustomToast(CreateRequestOvertTime.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi: " + t.getMessage());
                }
            });
        } else {
            String dataObjects = gson.toJson(groupRequestCreateRequest.getStatus());
            Log.d("CreateRequestOvertTime", "button action chi tiết: " + dataObjects);
            Call<ApiResponse<String>> call = apiInterface.modifyRequest(token, groupRequestCreateRequest);
            call.enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200) {
                            CustomToast.showCustomToast(CreateRequestOvertTime.this, apiResponse.getMessage());
                            startActivity(new Intent(CreateRequestOvertTime.this, RequestActivity.class));
                        } else {
                            CustomToast.showCustomToast(CreateRequestOvertTime.this, apiResponse.getMessage());
                        }
                    } else {
                        CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi kết nối, vui lòng thử lại.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    CustomToast.showCustomToast(CreateRequestOvertTime.this, "Lỗi: " + t.getMessage());
                }
            });
        }

    }

    public void onBackPressed() {
        super.onBackPressed(); // Quay lại trang trước đó
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}