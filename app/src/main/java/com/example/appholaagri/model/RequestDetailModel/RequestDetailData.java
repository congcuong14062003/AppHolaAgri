package com.example.appholaagri.model.RequestDetailModel;

import java.util.List;

public class RequestDetailData {
    private RequestGroup requestGroup;
    private int requestId;
    private String requestName;
    private RequestMethod requestMethod;
    private int dateType;
    private String startDate;
    private String startTime;
    private String startDateDisplay;
    private String endDate;
    private String endTime;
    private String endDateDisplay;
    private int duration;
    private String reason;
    private Status status;
    private Object fileAttachment;
    private String rejectReason;
    private Device device;
    private String contact;
    private int type;
    private Employee employee;
    private Department department;
    private Company company;
    private Division division;
    private List<RequestMethod> listMethod;
    private DirectManager directManager;
    private List<Consignee> consignee;
    private List<Follower> follower;
    private List<ListStatus> listStatus;
    private List<ApprovalLogs> approvalLogs;
    private List<ListApproveTime> listApproval;
    private String createdDate;
    private List<String> listApproveTime;
    private String dateTypeName;

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public Object getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(Object fileAttachment) {
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
}
