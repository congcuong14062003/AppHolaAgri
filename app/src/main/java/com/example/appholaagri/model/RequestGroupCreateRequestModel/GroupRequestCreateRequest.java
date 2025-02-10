package com.example.appholaagri.model.RequestGroupCreateRequestModel;

import java.util.List;
import java.util.Map;

public class GroupRequestCreateRequest {
    private String contact;
    private int dateType;
    private Device device;
    private int duration;
    private String endDate;
    private String endTime;
    private List<String> fileAttachment;
    private String reason;
    private String rejectReason;
    private RequestGroup requestGroup;
    private int requestId;
    private RequestMethod requestMethod;
    private String requestName;
    private String startDate;
    private String startTime;
    private Status status;
    private int type;
    // Getters and Setters
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class Device {
        private String additionalProp1;
        private String additionalProp2;
        private String additionalProp3;

        public Device(String code, int id, String name, int status) {
        }

        public Device(String additionalProp1, String additionalProp2, String additionalProp3) {
        }

        public String getAdditionalProp1() {
            return additionalProp1;
        }

        public void setAdditionalProp1(String additionalProp1) {
            this.additionalProp1 = additionalProp1;
        }

        public String getAdditionalProp2() {
            return additionalProp2;
        }

        public void setAdditionalProp2(String additionalProp2) {
            this.additionalProp2 = additionalProp2;
        }

        public String getAdditionalProp3() {
            return additionalProp3;
        }

        public void setAdditionalProp3(String additionalProp3) {
            this.additionalProp3 = additionalProp3;
        }
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(List<String> fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public RequestGroup getRequestGroup() {
        return requestGroup;
    }

    public void setRequestGroup(RequestGroup requestGroup) {
        this.requestGroup = requestGroup;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Nested classes for RequestGroup, RequestMethod, and Status
    public static class RequestGroup {
        private String code;
        private int id;
        private String name;
        private int status;

        public RequestGroup(String code, int id, String name, int status) {
            this.code = code;
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }

    public static class RequestMethod {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;
        private int index;

        public RequestMethod(String code, int id, String name, int status) {
            this.code = code;
            this.id = id;
            this.name = name;
            this.status = status;
        }

        // Getters and Setters
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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Status {
        private String code;
        private int id;
        private String name;
        private int status;

        public Status(String code, int id, String name, int status) {
            this.code = code;
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
