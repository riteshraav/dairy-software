package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "customerAdvance")
public class CustomerAdvance {
    @Id
    String id;
    Date date;
    String code;
    String name;
    double advanceAmount;
    String note;
    double interestRate;
    String paymentMethod;
    String adminId;
    private double remainingInterest;
    private Date recentDeduction;

    public void setId() {
        this.id = adminId+"_"+code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public double getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public double getRemainingInterest() {
        return remainingInterest;
    }

    public void setRemainingInterest(double remainingInterest) {
        this.remainingInterest = remainingInterest;
    }

    public Date getRecentDeduction() {
        return recentDeduction;
    }

    public void setRecentDeduction(Date recentDeduction) {
        this.recentDeduction = recentDeduction;
    }
}
