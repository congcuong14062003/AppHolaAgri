package com.example.appholaagri.model.RequestGroupCreateRequestModel;

import com.example.appholaagri.model.RequestDetailModel.RequestDetailData;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GroupRequestCreateRequest {
    private String contact;
    private int dateType;
    private Device device;
    private int duration;
    private String endDate;
    private String endTime;
    private Integer isUrgent;
    private BusinessTripFormReq businessTripFormReq;
    private List<ListDayReq> listDayReqs;
    private List<FileAttachment> fileAttachment;


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
    // Thêm thuộc tính customerContractReq
    private CustomerContractReq customerContractReq;

    // Phương thức gán customerContractReq từ RequestDetailData
    public void assignCustomerContractReq(RequestDetailData requestDetailData) {
        if (requestDetailData.getCustomerContractReq() != null) {
            CustomerContractReq customerContractReq = new CustomerContractReq(
                    requestDetailData.getCustomerContractReq().getCustomerContract(),
                    requestDetailData.getCustomerContractReq().getCustomerName(),
                    requestDetailData.getCustomerContractReq().getNote()
            );
            this.setCustomerContractReq(customerContractReq);
        }
    }
    // Getter và Setter cho customerContractReq
    public CustomerContractReq getCustomerContractReq() {
        return customerContractReq;
    }

    public void setCustomerContractReq(CustomerContractReq customerContractReq) {
        this.customerContractReq = customerContractReq;
    }

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

    public Integer getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Integer isUrgent) {
        this.isUrgent = isUrgent;
    }

    public List<ListDayReq> getListDayReqs() {
        return listDayReqs;
    }

    public void setListDayReqs(List<ListDayReq> listDayReqs) {
        this.listDayReqs = listDayReqs;
    }
    public BusinessTripFormReq getBusinessTripFormReq() {
        return businessTripFormReq;
    }

    public void setBusinessTripFormReq(BusinessTripFormReq businessTripFormReq) {
        this.businessTripFormReq = businessTripFormReq;
    }

    public List<FileAttachment> getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(List<FileAttachment> fileAttachment) {
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

    public static class ListDayReq {
        private List<BreakTime> breakTimes;
        private String day;
        private String endTime;
        private String startTime;

        public ListDayReq(List<BreakTime> breakTimes, String day, String startTime, String endTime) {
            this.breakTimes = breakTimes;
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    public static class BreakTime {
        private String startTime;
        private String endTime;

        public BreakTime(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
    public static class FileAttachment {
        private String name;
        private String path;
        public FileAttachment() {

        }
        public FileAttachment(String name, String path) {
            this.name = name;
            this.path = path;
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
    }

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
    public static class BusinessTripFormReq {
        private int totalCost;
        private String totalCostText;
        private String destination;
        private String content;

        public BusinessTripFormReq(int totalCost, String totalCostText, String destination, String content) {
            this.totalCost = totalCost;
            this.totalCostText = totalCostText;
            this.destination = destination;
            this.content = content;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(int totalCost) {
            this.totalCost = totalCost;
        }

        public String getTotalCostText() {
            return totalCostText;
        }

        public void setTotalCostText(String totalCostText) {
            this.totalCostText = totalCostText;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // Lớp CustomerContractReq
    public static class CustomerContractReq {
        private CustomerContract customerContract;
        private String customerName;
        private String note;

        public CustomerContractReq(CustomerContract customerContract, String customerName, String note) {
            this.customerContract = customerContract;
            this.customerName = customerName;
            this.note = note;
        }

        public CustomerContract getCustomerContract() {
            return customerContract;
        }

        public void setCustomerContract(CustomerContract customerContract) {
            this.customerContract = customerContract;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    // Lớp CustomerContract
    public static class CustomerContract {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;

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
    }
}
