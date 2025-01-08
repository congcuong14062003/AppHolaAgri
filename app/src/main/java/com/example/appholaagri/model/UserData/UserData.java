package com.example.appholaagri.model.UserData;

public class UserData {
    private int id;
    private AccountDetail accountDetail;
    private WorkInfor workInfo;
    private UserInfo userInfo;
    private ContractInfo contractInfo;
    private String userAvatar;
    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public WorkInfor getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfor workInfo) {
        this.workInfo = workInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

}
