package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Transient
    public static final String admin_code_seq = "admin_code_sequence";
    long code;
    String name;
    String dairyName;
    String city;
    String password;
    String subDistrict;
    String district;
    String state;
    @Id
    String id;
    long customerSequence;
    long supplierSequence;
    double currentBalance;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDairyName() {
        return dairyName;
    }

    public void setDairyName(String dairyName) {
        this.dairyName = dairyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCustomerSequence() {
        return customerSequence;
    }

    public void setCustomerSequence(long customerSequence) {
        this.customerSequence = customerSequence;
    }

    public long getSupplierSequence() {
        return supplierSequence;
    }

    public void setSupplierSequence(long supplierSequence) {
        this.supplierSequence = supplierSequence;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}