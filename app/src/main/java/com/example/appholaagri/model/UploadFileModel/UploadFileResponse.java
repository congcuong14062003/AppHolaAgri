package com.example.appholaagri.model.UploadFileModel;

import com.google.gson.annotations.SerializedName;

public class UploadFileResponse {
    @SerializedName("final_status")
    private int finalStatus;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("fileUrl")
    private String fileUrl;

    @SerializedName("file_url")
    private String file_url;

    @SerializedName("file_type")
    private String fileType;

    @SerializedName("file_size")
    private long fileSize;

    // Getters and Setters
    public int getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(int finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
