package com.example.appholaagri.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

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

public class FileManager {
    private final Context context;
    private final List<Uri> selectedFiles;
    private final List<RequestDetailData.FileAttachment> uploadedFiles;
    private final int maxFiles;
    private final long maxFileSize;
    private final FileManagerCallback callback;
    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000;
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public interface FileManagerCallback {
        void onShowLoading();
        void onHideLoading();
        void onShowToast(String message);
        void onFilesUpdated(List<Uri> selectedFiles, List<RequestDetailData.FileAttachment> uploadedFiles);
        void onUploadComplete();
    }

    public FileManager(Context context, int maxFiles, long maxFileSize, FileManagerCallback callback) {
        this.context = context;
        this.selectedFiles = new ArrayList<>();
        this.uploadedFiles = new ArrayList<>();
        this.maxFiles = maxFiles;
        this.maxFileSize = maxFileSize > 0 ? maxFileSize : DEFAULT_MAX_FILE_SIZE;
        this.callback = callback;
    }

    public void addFiles(List<Uri> newFiles, String token) {
        int availableSlots = maxFiles - selectedFiles.size();
        if (newFiles.size() > availableSlots) {
            callback.onShowToast("Chỉ có thể chọn tối đa " + availableSlots + " file!");
            newFiles = newFiles.subList(0, availableSlots);
        }
        selectedFiles.addAll(newFiles);
        callback.onFilesUpdated(selectedFiles, uploadedFiles);
        if (token != null) {
            uploadFilesSequentially(0, newFiles, new ArrayList<>(), token);
        } else {
            callback.onShowToast("Không tìm thấy token. Vui lòng đăng nhập lại!");
            callback.onHideLoading();
        }
    }

    public void removeFile(int index) {
        if (index >= 0 && index < selectedFiles.size()) {
            Uri fileUri = selectedFiles.remove(index);
            uploadedFiles.removeIf(attachment -> attachment.getPath().equals(fileUri.toString()));
            callback.onFilesUpdated(selectedFiles, uploadedFiles);
        }
    }

    public void syncUploadedFilesWithRequestDetailData(RequestDetailData requestDetailData) {
        if (requestDetailData == null) {
            Log.e("FileManager", "requestDetailData is null");
            return;
        }
        List<RequestDetailData.FileAttachment> updatedAttachments = new ArrayList<>();
        for (Uri fileUri : selectedFiles) {
            String filePath = fileUri.toString();
            RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
            RequestDetailData.FileAttachment existingAttachment = uploadedFiles.stream()
                    .filter(uploadFile -> uploadFile.getPath().equals(filePath))
                    .findFirst()
                    .orElse(null);
            if (existingAttachment != null) {
                attachment.setName(existingAttachment.getName());
                attachment.setPath(existingAttachment.getPath());
            } else {
                attachment.setName(getFileNameFromUri(fileUri));
                attachment.setPath(filePath);
            }
            updatedAttachments.add(attachment);
        }
        requestDetailData.setFileAttachment(updatedAttachments);
        if (updatedAttachments.size() != selectedFiles.size()) {
            Log.w("FileManager", "Mismatch between selectedFiles and updatedAttachments");
        }
    }

    private void uploadFilesSequentially(int index, List<Uri> newFiles, List<Uri> failedFiles, String token) {
        if (index >= newFiles.size()) {
            callback.onHideLoading();
            callback.onFilesUpdated(selectedFiles, uploadedFiles);
            if (!failedFiles.isEmpty()) {
                callback.onShowToast("Có " + failedFiles.size() + " file tải lên thất bại.");
            }
            callback.onUploadComplete();
            return;
        }

        Uri fileUri = newFiles.get(index);
        File file = getFileFromUri(context, fileUri);
        if (file == null || !isFileSizeValid(file)) {
            failedFiles.add(fileUri);
            callback.onShowToast("File " + getFileNameFromUri(fileUri) + " quá lớn hoặc không hợp lệ!");
            uploadFilesSequentially(index + 1, newFiles, failedFiles, token);
            return;
        }

        String fileName = getFileNameFromUri(fileUri);
        String mimeType = context.getContentResolver().getType(fileUri);
        String fileExtension = fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : "";
        String mediaType = getMediaType(fileExtension, mimeType);

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        callback.onShowLoading();
        ApiClient.getClient(context).create(ApiInterface.class)
                .uploadFile(token, body)
                .enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                        handleUploadResponse(response, call, fileUri, fileName, index, newFiles, failedFiles, token);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                        handleUploadFailure(call, fileUri, fileName, index, newFiles, failedFiles, token, 0);
                    }
                });
    }

    private void handleUploadResponse(Response<ApiResponse<UploadFileResponse>> response,
                                      Call<ApiResponse<UploadFileResponse>> call, Uri fileUri, String fileName,
                                      int index, List<Uri> newFiles, List<Uri> failedFiles, String token) {
        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
            UploadFileResponse uploadResponse = response.body().getData();
            String fileUrl = "https://haloship.imediatech.com.vn/" + uploadResponse.getFileUrl();
            RequestDetailData.FileAttachment attachment = new RequestDetailData.FileAttachment();
            attachment.setName(fileName);
            attachment.setPath(fileUrl);

            int selectedIndex = selectedFiles.indexOf(fileUri);
            if (selectedIndex != -1) {
                selectedFiles.set(selectedIndex, Uri.parse(fileUrl));
            }
            uploadedFiles.removeIf(uploadFile -> uploadFile.getPath().equals(fileUri.toString()));
            uploadedFiles.add(attachment);

            uploadFilesSequentially(index + 1, newFiles, failedFiles, token);
        } else {
            handleUploadFailure(call, fileUri, fileName, index, newFiles, failedFiles, token, 0);
        }
    }

    private void handleUploadFailure(Call<ApiResponse<UploadFileResponse>> call, Uri fileUri, String fileName,
                                     int index, List<Uri> newFiles, List<Uri> failedFiles, String token, int retryAttempt) {
        if (retryAttempt < MAX_RETRIES) {
            try {
                Thread.sleep(RETRY_DELAY_MS);
                call.clone().enqueue(new Callback<ApiResponse<UploadFileResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UploadFileResponse>> call, Response<ApiResponse<UploadFileResponse>> response) {
                        handleUploadResponse(response, call, fileUri, fileName, index, newFiles, failedFiles, token);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UploadFileResponse>> call, Throwable t) {
                        handleUploadFailure(call, fileUri, fileName, index, newFiles, failedFiles, token, retryAttempt + 1);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failedFiles.add(fileUri);
                uploadFilesSequentially(index + 1, newFiles, failedFiles, token);
            }
        } else {
            failedFiles.add(fileUri);
            uploadFilesSequentially(index + 1, newFiles, failedFiles, token);
        }
    }

    private String getMediaType(String fileExtension, String mimeType) {
        if (mimeType != null) return mimeType;
        switch (fileExtension) {
            case "pdf": return "application/pdf";
            case "doc":
            case "docx": return "application/msword";
            case "xls":
            case "xlsx": return "application/vnd.ms-excel";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            default: return "*/*";
        }
    }

    public String getFileNameFromUri(Uri uri) {
        String fileName = null;
        ContentResolver resolver = context.getContentResolver();
        if (uri.getScheme() != null) {
            if ("content".equals(uri.getScheme())) {
                try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (nameIndex != -1) {
                            fileName = cursor.getString(nameIndex);
                        }
                    }
                }
                if (fileName == null) {
                    fileName = uri.getLastPathSegment();
                }
            } else if ("file".equals(uri.getScheme())) {
                fileName = uri.getLastPathSegment();
            } else if (uri.toString().startsWith("http")) {
                String path = uri.getPath();
                if (path != null) {
                    fileName = path.substring(path.lastIndexOf('/') + 1);
                }
            }
        }
        if (fileName == null) {
            String mimeType = resolver.getType(uri);
            String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            fileName = "file_" + System.currentTimeMillis() + (extension != null ? "." + extension : "");
        }
        return fileName;
    }

    private boolean isFileSizeValid(File file) {
        return file.length() <= maxFileSize;
    }

    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            String fileName = "temp_" + System.currentTimeMillis() + ".tmp";
            File tempFile = new File(context.getCacheDir(), fileName);
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                if (inputStream != null) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    file = tempFile;
                }
            }
        } catch (IOException e) {
            Log.e("FileManager", "Error getting file from URI: " + e.getMessage());
        }
        return file;
    }

    public List<Uri> getSelectedFiles() {
        return new ArrayList<>(selectedFiles);
    }

    public List<RequestDetailData.FileAttachment> getUploadedFiles() {
        return new ArrayList<>(uploadedFiles);
    }

    public void clearFiles() {
        selectedFiles.clear();
        uploadedFiles.clear();
        callback.onFilesUpdated(selectedFiles, uploadedFiles);
    }
}