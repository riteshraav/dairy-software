package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document(collection = "loan_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntry {
    @Id
    String _id;
    Date date;
    String customerId;
    String adminId;
    double loanAmount;
    String note;
    double interestRate;
    String modeOfPayback;
    double remainingInterest;
    Date recentDeduction;
    public void set_id()
    {
        _id = adminId + "_"+customerId;
    }

    public String get_id() {
        return _id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
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

    public String getModeOfPayback() {
        return modeOfPayback;
    }

    public void setModeOfPayback(String modeOfPayback) {
        this.modeOfPayback = modeOfPayback;
    }
}
