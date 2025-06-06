package com.example.appholaagri.model.RequestDetailModel;

import com.example.appholaagri.model.RequestGroupCreateRequestModel.GroupRequestCreateRequest;

import java.util.List;

public class RequestDetailData {
    private RequestGroup requestGroup;
    private int requestId;
    private String requestName;
    private Integer isUrgent;
    private RequestMethod requestMethod;
    private int dateType;
    private String startDate;
    private String startTime;
    private String startDateDisplay;
    private String endDate;
    private String endTime;
    private String endDateDisplay;
    private Double duration;
    private String reason;
    private Status status;
    private List<FileAttachment> fileAttachment; // Sửa thành List
    private String rejectReason;
    private Device device;
    private String contact;
    private int type;
    private Employee employee;
    private Department department;
    private Company company;
    private Division division;
    private List<ListDayReq> listDayReqs;
    private List<RequestMethod> listMethod;
    private BusinessTripFormReq businessTripFormReq;
    private DirectManager directManager;
    private List<Consignee> consignee;
    private List<Follower> follower;
    private List<ListStatus> listStatus;
    private List<ApprovalLogs> approvalLogs;
    private List<ListApproveTime> listApproval;
    private String createdDate;
    private List<String> listApproveTime;
    private String dateTypeName;

    private RecruitmentReq recruitmentReq;

    // Thêm thuộc tính customerContractReq
    private GroupRequestCreateRequest.CustomerContractReq customerContractReq;


    // Getter và Setter cho customerContractReq
    // Getter và Setter cho customerContractReq
    public GroupRequestCreateRequest.CustomerContractReq getCustomerContractReq() {
        return customerContractReq;
    }

    public void setCustomerContractReq(GroupRequestCreateRequest.CustomerContractReq customerContractReq) {
        this.customerContractReq = customerContractReq;
    }

    // Getters and Setters
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

    public String getRequestName() {
        return requestName;
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

    public BusinessTripFormReq getBusinessTripFormReq() {
        return businessTripFormReq;
    }

    public void setBusinessTripFormReq(BusinessTripFormReq businessTripFormReq) {
        this.businessTripFormReq = businessTripFormReq;
    }

    public void setListDayReqs(List<ListDayReq> listDayReqs) {
        this.listDayReqs = listDayReqs;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
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

    public String getStartDateDisplay() {
        return startDateDisplay;
    }

    public void setStartDateDisplay(String startDateDisplay) {
        this.startDateDisplay = startDateDisplay;
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

    public String getEndDateDisplay() {
        return endDateDisplay;
    }

    public void setEndDateDisplay(String endDateDisplay) {
        this.endDateDisplay = endDateDisplay;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Getter và Setter
    public List<FileAttachment> getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(List<FileAttachment> fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<RequestMethod> getListMethod() {
        return listMethod;
    }

    public void setListMethod(List<RequestMethod> listMethod) {
        this.listMethod = listMethod;
    }

    public DirectManager getDirectManager() {
        return directManager;
    }

    public void setDirectManager(DirectManager directManager) {
        this.directManager = directManager;
    }

    public List<Consignee> getConsignee() {
        return consignee;
    }

    public void setConsignee(List<Consignee> consignee) {
        this.consignee = consignee;
    }

    public List<Follower> getFollower() {
        return follower;
    }

    public void setFollower(List<Follower> follower) {
        this.follower = follower;
    }

    public List<ListStatus> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<ListStatus> listStatus) {
        this.listStatus = listStatus;
    }

    public List<ApprovalLogs> getApprovalLogs() {
        return approvalLogs;
    }

    public void setApprovalLogs(List<ApprovalLogs> approvalLogs) {
        this.approvalLogs = approvalLogs;
    }

    public List<ListApproveTime> getListApproval() {
        return listApproval;
    }

    public void setListApproval(List<ListApproveTime> listApproval) {
        this.listApproval = listApproval;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getListApproveTime() {
        return listApproveTime;
    }

    public void setListApproveTime(List<String> listApproveTime) {
        this.listApproveTime = listApproveTime;
    }

    public String getDateTypeName() {
        return dateTypeName;
    }

    public void setDateTypeName(String dateTypeName) {
        this.dateTypeName = dateTypeName;
    }

    public RecruitmentReq getRecruitmentReq() {
        return recruitmentReq;
    }

    public void setRecruitmentReq(RecruitmentReq recruitmentReq) {
        this.recruitmentReq = recruitmentReq;
    }

    public static class BusinessTripFormReq {
        private int totalCost;
        private String totalCostText;
        private String destination;
        private String content;

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

    public static class FileAttachment {
        private String name;
        private String path;

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

    // Lớp CustomerContractReq
    public static class CustomerContractReq {
        private CustomerContract customerContract;
        private String customerName;
        private String note;

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

    public static class Status {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;
        private Object index;
        public Status(int id, String code, String name, int status) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.status = status;
        }

        public Status(int id, String code, String name, int status, String color, Object index) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.status = status;
            this.color = color;
            this.index = index;
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

        public Object getIndex() {
            return index;
        }

        public void setIndex(Object index) {
            this.index = index;
        }
    }
    public static class RecruitmentReq {
        private Position position;
        private int quantity;
        private long salary;
        private String description;
        private String requirement;

        // Getter and Setter for position
        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        // Getter and Setter for quantity
        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        // Getter and Setter for salary
        public long getSalary() {
            return salary;
        }

        public void setSalary(long salary) {
            this.salary = salary;
        }

        // Getter and Setter for description
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // Getter and Setter for requirement
        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }
        public static class Position {
            private int id;
            private String code;
            private String name;
            private int status;
            private String color;
            private String index; // Có thể là null, nên để kiểu String

            // Getter and Setter for id
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            // Getter and Setter for code
            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            // Getter and Setter for name
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            // Getter and Setter for status
            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            // Getter and Setter for color
            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            // Getter and Setter for index
            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }
        }
    }
}