package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer{
    @Id
    private String _id;
    private String code;
    private String name;
    private String phone;
    private boolean buffalo;
    private boolean cow;
    private String adminId;
    private String classType;

    private  String branchName;

    private String gender;

    private  String caste;

    private String alternateNumber;

    private  String email;

    private String accountNo;

    private  String bankCode;

    private  String sabhasadNo;

    private String bankBranchName;

    private String bankAccountNo;

    private String ifscNo;

    private  String aadharNo;

    private String panNo;

    private int animalCount;

    private double averageMilk;
    public void setId(){
        this._id = this.adminId+"_"+this.code;
    }
    public String getPhone(){
        return phone;
    }
    public String getAdminId(){
        return adminId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBuffalo() {
        return buffalo;
    }

    public void setBuffalo(boolean buffalo) {
        this.buffalo = buffalo;
    }

    public boolean isCow() {
        return cow;
    }

    public void setCow(boolean cow) {
        this.cow = cow;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSabhasadNo() {
        return sabhasadNo;
    }

    public void setSabhasadNo(String sabhasadNo) {
        this.sabhasadNo = sabhasadNo;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getIfscNo() {
        return ifscNo;
    }

    public void setIfscNo(String ifscNo) {
        this.ifscNo = ifscNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public void setAnimalCount(int animalCount) {
        this.animalCount = animalCount;
    }

    public double getAverageMilk() {
        return averageMilk;
    }

    public void setAverageMilk(double averageMilk) {
        this.averageMilk = averageMilk;
    }
}
