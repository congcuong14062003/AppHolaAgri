package com.example.appholaagri.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appholaagri.R;
import com.example.appholaagri.adapter.ImageHorizontalAdapter;
import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RecordConditionModel.InforRecordConditionByQrCode;
import com.example.appholaagri.model.RecordConditionModel.RecordConditionTabList;
import com.example.appholaagri.model.RequestTabListData.RequestTabListData;
import com.example.appholaagri.model.RequestTabListData.RequestTabListDataResponse;
import com.example.appholaagri.model.RequestTabListData.RequestTabListRequest;
import com.example.appholaagri.model.UploadFileModel.UploadFileResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;
import com.example.appholaagri.utils.CustomToast;
import com.example.appholaagri.utils.KeyboardUtils;
import com.example.appholaagri.utils.LoadingDialog;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrContentRecordCondition extends AppCompatActivity {
    TextView plantation_name, area_name, type_plant, code_plant,
            // detail
    sender_plantation, employee_code, team_code, time, problem_detail, infor_detail;
            ;
    private List<Uri> selectedImages = new ArrayList<>();
    private FlexboxLayout imageContainer;
    private final int MAX_IMAGES = 10;
    private static final int PICK_IMAGES = 100;
    private static final int TAKE_PHOTO = 101;
    private Uri photoUri;
    private AppCompatButton send_information;
    private InforRecordConditionByQrCode inforRecordConditionByQrCode = new InforRecordConditionByQrCode();
    private LoadingDialog loadingDialog; // Context là activity hoặc fragment của bạn
    private ImageView backBtnReview;
    EditText edt_problem_disease, edt_development_information;
    LinearLayout buttonContainer,employee_infor_detail, status_infor_detail, problem_disease_layout, development_information_layout,
            file_layout, file_layout_detail

    ; // hoặc findViewById nếu là activity
    private int recordId;
    private RecyclerView recyclerViewImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_content_record_condition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo LoadingDialog
        plantation_name = findViewById(R.id.plantation_name);
        area_name = findViewById(R.id.area_name);
        type_plant = findViewById(R.id.type_plant);
        code_plant = findViewById(R.id.code_plant);
        imageContainer = findViewById(R.id.image_container);
        backBtnReview = findViewById(R.id.backBtnReview);
        recyclerViewImages = findViewById(R.id.recycler_view_images_detail);
        // detail
        sender_plantation = findViewById(R.id.sender_plantation);
        employee_code = findViewById(R.id.employee_code);
        team_code = findViewById(R.id.team_code);
        time = findViewById(R.id.time);
        problem_detail = findViewById(R.id.problem_detail);
        infor_detail = findViewById(R.id.infor_detail);
        file_layout = findViewById(R.id.file_layout);
        file_layout_detail = findViewById(R.id.file_layout_detail);


        employee_infor_detail = findViewById(R.id.employee_infor_detail);
        status_infor_detail = findViewById(R.id.status_infor_detail);
        problem_disease_layout = findViewById(R.id.problem_disease_layout);
        development_information_layout = findViewById(R.id.development_information_layout);



        edt_problem_disease = findViewById(R.id.edt_problem_disease); // hoặc findViewById nếu là Activity
        edt_development_information = findViewById(R.id.edt_development_information); // hoặc findViewById nếu là Activity
        buttonContainer = findViewById(R.id.button_action);
        loadingDialog = new LoadingDialog(this);
        Intent intent = getIntent();
        String qrResult = intent.getStringExtra("qrResult");


        // Nhận object tab_data
        RecordConditionTabList tabData = (RecordConditionTabList) intent.getSerializableExtra("tab_data");


        // Nhận id từ Intent
        if (getIntent() != null) {
            recordId = getIntent().getIntExtra("record_id", -1); // Lấy id của item
            Log.d("record_id", "record_id" + recordId);
            if(recordId != -1) {
                employee_infor_detail.setVisibility(View.VISIBLE);
                status_infor_detail.setVisibility(View.VISIBLE);
                problem_disease_layout.setVisibility(View.GONE);
                development_information_layout.setVisibility(View.GONE);
                file_layout.setVisibility(View.GONE);
                file_layout_detail.setVisibility(View.VISIBLE);
                getDetailRecord(recordId);
            }
        }

        if(qrResult != null) {
            handleGetInit(qrResult);
        }
        renderImages(); // Hiển thị khởi tạo

        backBtnReview.setOnClickListener(view -> {
            finish();
        });
    }
    public void handleGetInit(String qrResult) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<InforRecordConditionByQrCode>> call = apiInterface.qrContentRecordCondition(token, qrResult);
        call.enqueue(new Callback<ApiResponse<InforRecordConditionByQrCode>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<InforRecordConditionByQrCode>> call, Response<ApiResponse<InforRecordConditionByQrCode>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<InforRecordConditionByQrCode> apiResponse = response.body();
                    if(apiResponse.getStatus() == 200) {
                        inforRecordConditionByQrCode = apiResponse.getData();
                        updateUI(inforRecordConditionByQrCode); // Cập nhật danh sách tab vào Activity
                    } else {
                        Log.e("QrContentRecordCondition", "API response unsuccessful");
                    }
                } else {
                    Log.e("QrContentRecordCondition", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InforRecordConditionByQrCode>> call, Throwable t) {
                Log.e("QrContentRecordCondition", "Error: " + t.getMessage());
            }
        });
    }

    public void getDetailRecord(int recordId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<InforRecordConditionByQrCode>> call = apiInterface.qrContentDetailRecordCondition(token, recordId);
        call.enqueue(new Callback<ApiResponse<InforRecordConditionByQrCode>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<InforRecordConditionByQrCode>> call, Response<ApiResponse<InforRecordConditionByQrCode>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<InforRecordConditionByQrCode> apiResponse = response.body();
                    if(apiResponse.getStatus() == 200) {
                        inforRecordConditionByQrCode = apiResponse.getData();
                        updateUI(inforRecordConditionByQrCode); // Cập nhật danh sách tab vào Activity
                    } else {
                        Log.e("QrContentRecordCondition", "API response unsuccessful");
                    }
                } else {
                    Log.e("QrContentRecordCondition", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InforRecordConditionByQrCode>> call, Throwable t) {
                Log.e("QrContentRecordCondition", "Error: " + t.getMessage());
            }
        });
    }


    public void updateUI(InforRecordConditionByQrCode inforRecordConditionByQrCode) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String inforRecordJson = gson.toJson(inforRecordConditionByQrCode);
        Log.d("aaaaaa", "data: " + inforRecordJson);
        plantation_name.setText(inforRecordConditionByQrCode.getPlantation().getName());
        area_name.setText(inforRecordConditionByQrCode.getCultivationArea().getName());
        type_plant.setText(inforRecordConditionByQrCode.getCropVarieties().getName());
        code_plant.setText(inforRecordConditionByQrCode.getPlantCode());

        if(inforRecordConditionByQrCode.getProblem() != null) {
            edt_problem_disease.setText(inforRecordConditionByQrCode.getProblem());
            problem_detail.setText(inforRecordConditionByQrCode.getProblem());
        }

        if(inforRecordConditionByQrCode.getDevelopment() != null) {
            edt_development_information.setText(inforRecordConditionByQrCode.getDevelopment());
            infor_detail.setText(inforRecordConditionByQrCode.getDevelopment());
        }

        if(inforRecordConditionByQrCode.getEmployee() != null) {
            sender_plantation.setText(inforRecordConditionByQrCode.getEmployee().getName());
            employee_code.setText(inforRecordConditionByQrCode.getEmployee().getCode());
            team_code.setText(inforRecordConditionByQrCode.getTeam().getName());
            time.setText(inforRecordConditionByQrCode.getEmployee().getAtDate());
        }

        if (inforRecordConditionByQrCode.getFileAttachment() != null) {
            recyclerViewImages.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );

            List<String> imageUrls = inforRecordConditionByQrCode.getFileAttachment(); // Giả sử là List<String>
            ImageHorizontalAdapter adapter = new ImageHorizontalAdapter(this, imageUrls);
            recyclerViewImages.setAdapter(adapter);
        }
        for (InforRecordConditionByQrCode.ListStatus status : inforRecordConditionByQrCode.getListStatus()) {
            AppCompatButton button = new AppCompatButton(this); // dùng this nếu trong Activity

            // Set text và padding
            button.setText(status.getName());
            button.setTextColor(Color.WHITE);
            button.setTextSize(14);
            button.setAllCaps(false);
            button.setPadding(40, 20, 40, 20);

            // Set background động
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(status.getColor())));
            button.setBackgroundResource(R.drawable.button_custom); // nếu bạn muốn giữ bo góc
            button.setStateListAnimator(null); // tắt animation mặc định

            // Set layout params
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10); // khoảng cách giữa các nút
            button.setLayoutParams(params);

            // Optional: Set ID để biết nút nào được bấm
            button.setId(View.generateViewId());

            // Thêm vào trong onClickListener của bạn
            button.setOnClickListener(v -> {
                // Tạo một đối tượng Status từ ListStatus
                InforRecordConditionByQrCode.Status selectedStatus = new InforRecordConditionByQrCode.Status();
                selectedStatus.setId(status.getId());
                selectedStatus.setCode(status.getCode());
                selectedStatus.setName(status.getName());
                selectedStatus.setStatus(status.getStatus());

                // Cập nhật đối tượng InforRecordConditionByQrCode với trạng thái đã chọn
                inforRecordConditionByQrCode.setStatus(selectedStatus);

                // Gửi dữ liệu sau khi cập nhật trạng thái
                handleSendData();
            });
            // Thêm nút vào container
            buttonContainer.addView(button);
        }
    }



        // xử lý ảnh
        private void renderImages() {
            imageContainer.removeAllViews();

            for (int i = 0; i < selectedImages.size(); i++) {
                Uri imageUri = selectedImages.get(i);

                // Inflate layout item_image_preview.xml
                View itemView = getLayoutInflater().inflate(R.layout.item_image_preview, imageContainer, false);
                ImageView imageView = itemView.findViewById(R.id.image);
                ImageView btnDelete = itemView.findViewById(R.id.btn_delete);

                imageView.setImageURI(imageUri);
                int finalI = i;
                btnDelete.setOnClickListener(v -> {
                    selectedImages.remove(finalI);
                    renderImages();
                });

                imageContainer.addView(itemView);
            }

            // Thêm nút "Thêm ảnh"
            if (selectedImages.size() < MAX_IMAGES) {
                View addView = getLayoutInflater().inflate(R.layout.item_add_image, imageContainer, false);
                addView.setOnClickListener(v -> pickImages());
                imageContainer.addView(addView);
            }
        }

        private void pickImages() {
            String[] options = {"Chụp ảnh", "Chọn từ thư viện"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm ảnh")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            openCamera();
                        } else {
                            openGallery();
                        }
                    })
                    .show();
        }
        private void openGallery() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGES);
        }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Kiểm tra xem thiết bị có hỗ trợ camera không
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                String fileName = "IMG_" + System.currentTimeMillis();
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // Đảm bảo lưu tệp vào thư mục thích hợp
                if (storageDir != null && !storageDir.exists()) {
                    storageDir.mkdirs(); // Đảm bảo thư mục tồn tại
                }
                photoFile = File.createTempFile(fileName, ".jpg", storageDir); // Tạo tệp ảnh tạm trong thư mục này

                // Cấp quyền cho FileProvider
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, TAKE_PHOTO);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("CameraError", "Error creating file for camera capture", ex);
            }
        } else {
            Log.e("CameraError", "No camera application found");
        }
    }



    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            // Xử lý ảnh chọn từ thư viện
            if (requestCode == PICK_IMAGES && resultCode == RESULT_OK && data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count && selectedImages.size() < MAX_IMAGES; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        selectedImages.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    if (selectedImages.size() < MAX_IMAGES) {
                        selectedImages.add(data.getData());
                    }
                }
                renderImages();
                updateFileAttachments();
            }

            // Xử lý ảnh từ camera
            if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK && photoUri != null) {
                if (selectedImages.size() < MAX_IMAGES) {
                    selectedImages.add(photoUri);  // Thêm ảnh từ camera vào selectedImages
                    renderImages(); // Cập nhật UI để hiển thị ảnh
                }
                updateFileAttachments();
            }
        }


        private void updateFileAttachments() {
            // Duyệt qua danh sách các Uri, chuyển thành đường dẫn URL cho fileAttachment
            List<String> fileAttachmentUrls = new ArrayList<>();
            for (Uri uri : selectedImages) {
                // Giả sử ảnh đã được upload và bạn có URL của ảnh
                // Thêm URL ảnh vào danh sách
                fileAttachmentUrls.add(getFileNameFromUri(uri));
            }
            // Cập nhật lại fileAttachment trong đối tượng InforRecordConditionByQrCode
            inforRecordConditionByQrCode.setFileAttachment(fileAttachmentUrls);
            // Log ra fileAttachment
            // Serialize toàn bộ đối tượng inforRecordConditionByQrCode thành chuỗi JSON

            // Log ra toàn bộ đối tượng
        }

        // Phương thức để lấy tên file từ Uri (giả sử bạn có cách upload ảnh và lấy đường dẫn URL tương ứng)
        private String getFileNameFromUri(Uri uri) {
            // Giải pháp đơn giản để lấy tên file từ Uri (cần điều chỉnh cho phù hợp với cấu trúc thực tế)
            return uri.getLastPathSegment();
        }

        public void handleSendData() {
            Log.d("QrContentRecordCondition", "Bắt đầu gửi dữ liệu và upload ảnh");
            // Hiển thị loading dialog khi bắt đầu upload
            loadingDialog.show();
            // Thu thập dữ liệu từ các trường nhập liệu


            inforRecordConditionByQrCode.setProblem(edt_problem_disease.getText().toString());
            inforRecordConditionByQrCode.setDevelopment(edt_development_information.getText().toString());

            // Upload từng ảnh trước khi gọi confirmRecordCondition
            uploadImagesSequentially(0, new ArrayList<>());
        }


    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            // Tạo file tạm từ Uri
            String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(context.getCacheDir(), fileName);

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead); // ✔️
                }
                outputStream.close();
                inputStream.close();
                file = tempFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public String getRealPathFromURI(Uri uri) {
            String path = null;
            String[] projection = {MediaStore.Images.Media.DATA}; // Chỉ lấy cột '_data'

            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Trường hợp cột '_data' không tồn tại, xử lý theo cách khác
            if (path == null) {
                path = uri.getPath();
            }

            return path;
        }
        private void uploadImagesSequentially(int index, List<String> uploadedUrls) {
            if (index >= selectedImages.size()) {
                // Upload xong hết => gọi API chính
                inforRecordConditionByQrCode.setFileAttachment(uploadedUrls);
                sendRequestToServer(inforRecordConditionByQrCode);

                // Ẩn loading dialog sau khi tất cả file đã được upload
                loadingDialog.hide();
                return;
            }

            Uri imageUri = selectedImages.get(index);
            File file = getFileFromUri(this, imageUri);

            Log.d("kkkk", "file: " + file);
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                Log.d("kkkk", "file: " + "Vào");
                SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("auth_token", null);
                ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

                // Gọi API upload ảnh
                Call<ApiResponse<UploadFileResponse>> call = apiInterface.uploadFile(token, body);
                call.enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                        loadingDialog.hide();
                        Log.d("QrContentRecordCondition", "Response: " +response);
                        if (response.isSuccessful() && response.body() != null) {
                            String relativePath = response.body().getData().getFileUrl(); // Trả về dạng imedia/...
                            String fileUrl = "https://haloship.imediatech.com.vn/" + relativePath;
                            Log.d("kkkk", "relativePath: "+ relativePath);
                            uploadedUrls.add(fileUrl); // Thêm URL vào danh sách
                            // Tiếp tục upload ảnh tiếp theo
                            uploadImagesSequentially(index + 1, uploadedUrls);
                        } else {
                            Log.e("Upload Failed", "Server returned error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                        loadingDialog.hide();
                        Log.e("Upload Failed", t.getMessage(), t);
                    }
                });
            } else {
                Log.e("FileError", "Tệp không tồn tại tại: " + file.getAbsolutePath());
                uploadImagesSequentially(index + 1, uploadedUrls);  // Tiến hành với ảnh tiếp theo ngay cả khi tệp không hợp lệ
            }
        }
    private void sendRequestToServer(InforRecordConditionByQrCode request) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        String inforRecordJson = gson.toJson(request);
        Log.d("ffff", "dữ liệu tạo: " + inforRecordJson);
        loadingDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ApiResponse<String>> call = apiInterface.confirmRecordCondition(token, request);
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                loadingDialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        Log.d("QrContentRecordCondition", "Record condition confirmed successfully.");
                        CustomToast.showCustomToast(QrContentRecordCondition.this, apiResponse.getMessage());
                        Intent intent = new Intent(QrContentRecordCondition.this, RecordConditionActivity.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Log.e("QrContentRecordCondition", "API response unsuccessful 1");
                        CustomToast.showCustomToast(QrContentRecordCondition.this, apiResponse.getMessage());
                    }
                } else {
                    Log.e("QrContentRecordCondition", "API response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                loadingDialog.hide();
                Log.e("QrContentRecordCondition", "Error: " + t.getMessage());
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        KeyboardUtils.hideKeyboardOnTouchOutside(this, event);
        return super.dispatchTouchEvent(event);
    }
}