package com.example.appholaagri.helper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.appholaagri.model.ApiResponse.ApiResponse;
import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;
import com.example.appholaagri.model.UploadFileModel.UploadFileResponse;
import com.example.appholaagri.service.ApiClient;
import com.example.appholaagri.service.ApiInterface;

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

public class FileUploadHelper {
    public static final int PICK_FILES_REQUEST_CODE = 200;
    private final Activity activity;
    private final OnFileUploadListener listener;

    public interface OnFileUploadListener {
        void onFileSelected(List<Uri> selectedFiles);
        void onFileUploaded(RequestDetailData.FileAttachment attachment);
        void onUploadError(String errorMessage);
        void onUploadComplete();
    }

    public FileUploadHelper(Activity activity, OnFileUploadListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    // Mở giao diện chọn file
    public void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Chọn tệp"), PICK_FILES_REQUEST_CODE);
    }

    // Xử lý kết quả chọn file
    public void handleActivityResult(int requestCode, int resultCode, Intent data, List<Uri> selectedFiles) {
        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<Uri> newFiles = new ArrayList<>();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileNameFromUri(fileUri);
                    Log.d("FileSelect", "URI: " + fileUri + ", Tên file: " + fileName);
                    newFiles.add(fileUri);
                    selectedFiles.add(fileUri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                String fileName = getFileNameFromUri(fileUri);
                Log.d("FileSelect", "URI: " + fileUri + ", Tên file: " + fileName);
                newFiles.add(fileUri);
                selectedFiles.add(fileUri);
            }
            if (!newFiles.isEmpty()) {
                listener.onFileSelected(newFiles);
                uploadFilesSequentially(0, newFiles);
            }
        }
    }

    // Upload file tuần tự
    private void uploadFilesSequentially(int index, List<Uri> newFiles) {
        if (index >= newFiles.size()) {
            listener.onUploadComplete();
            return;
        }

        Uri fileUri = newFiles.get(index);
        File file = getFileFromUri(activity, fileUri);
        if (file == null) {
            uploadFilesSequentially(index + 1, newFiles);
            return;
        }

        String fileName = getFileNameFromUri(fileUri);
        Log.d("Upload", "Uploading file: " + fileName);
        String mimeType = activity.getContentResolver().getType(fileUri);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String mediaType = "*/*";
        if ("pdf".equals(fileExtension)) mediaType = "application/pdf";
        else if ("doc".equals(fileExtension) || "docx".equals(fileExtension)) mediaType = "application/msword";
        else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) mediaType = "application/vnd.ms-excel";

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token == null) {
            listener.onUploadError("Token không tồn tại, vui lòng đăng nhập lại!");
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient(activity).create(ApiInterface.class);
        Call<ApiResponse<UploadFileResponse>> call = apiInterface.uploadFile(token, body);
        call.enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    UploadFileResponse uploadResponse = response.body().getData();
                    if (uploadResponse.getFinalStatus() == 200) {
                        String fileUrl = "https://haloship.imediatech.com.vn/" + uploadResponse.getFileUrl();
                        RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
                        attachment.setName(fileName);
                        attachment.setPath(fileUrl);
                        attachment.setStatus(1);
                        listener.onFileUploaded(attachment);
                        uploadFilesSequentially(index + 1, newFiles);
                    } else {
                        listener.onUploadError("Lỗi tải file: " + uploadResponse.getMessage());
                    }
                } else {
                    listener.onUploadError("Lỗi server: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                listener.onUploadError("Lỗi: " + t.getMessage());
            }
        });
    }

    // Lấy tên file từ Uri
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        if (fileName == null || !fileName.contains(".")) {
            String mimeType = activity.getContentResolver().getType(uri);
            if (mimeType != null) {
                if (mimeType.contains("msword")) fileName = "document_" + System.currentTimeMillis() + ".doc";
                else if (mimeType.contains("vnd.openxmlformats-officedocument.wordprocessingml")) fileName = "document_" + System.currentTimeMillis() + ".docx";
                else if (mimeType.contains("pdf")) fileName = "document_" + System.currentTimeMillis() + ".pdf";
                else fileName = "unknown_file_" + System.currentTimeMillis();
            } else {
                fileName = "unknown_file_" + System.currentTimeMillis();
            }
        }
        if (fileName != null && !fileName.contains(".")) {
            fileName += ".bin";
        }
        return fileName;
    }

    // Chuyển Uri thành File
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            String fileName = "temp_" + System.currentTimeMillis() + ".tmp";
            File tempFile = new File(context.getCacheDir(), fileName);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                file = tempFile;
            }
        } catch (IOException e) {
            Log.e("FileUploadHelper", "Error converting Uri to File: " + e.getMessage());
        }
        return file;
    }
}