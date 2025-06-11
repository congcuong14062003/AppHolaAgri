package com.example.appholaagri.model.RequestDetailModel;

import java.util.List;

public class Comments {
    private int id;
    private User user;
    private String comment;
    private String createdDate;
    private List<FileAttachment> fileAttachments;
    private int requestId;

    // Constructors
    public Comments() {}
    public Comments(String comment, List<FileAttachment> fileAttachments, int id,  int requestId) {
        this.comment = comment;
        this.fileAttachments = fileAttachments;
        this.id = id;
        this.requestId = requestId;
    }
    public Comments(int id, User user, String comment, String createdDate, List<FileAttachment> fileAttachments) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.createdDate = createdDate;
        this.fileAttachments = fileAttachments;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<FileAttachment> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<FileAttachment> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    // Inner Classes
    public static class User {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;
        private String url;

        public User() {}

        public User(int id, String code, String name, int status, int index, String url) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.status = status;
            this.index = index;
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class FileAttachment {
        private String name;
        private String path;
        private int id;
        private int status;

        public FileAttachment() {}

        public FileAttachment(String name, String path, int id, int status) {
            this.name = name;
            this.path = path;
            this.id = id;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

