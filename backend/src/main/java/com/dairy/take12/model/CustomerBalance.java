package com.dairy.take12.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "customer_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerBalance {
    @Id
    private String _id;
    private String adminId;
    private String customerId;
    private double balanceCattleFeed;
    private double balanceAdvance;
    private double balanceCreditMilk;
    private double balanceLoan;
    private double balanceOtherExpense;
    private double balanceDoctorVisitingFees;
    private double balanceExpense;
    private double totalBalance;


    public CustomerBalance(String _id)
    {
       this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getBalanceCattleFeed() {
        return balanceCattleFeed;
    }

    public void setBalanceCattleFeed(double balanceCattleFeed) {
        this.balanceCattleFeed = balanceCattleFeed;
    }

    public double getBalanceAdvance() {
        return balanceAdvance;
    }

    public void setBalanceAdvance(double balanceAdvance) {
        this.balanceAdvance = balanceAdvance;
    }

    public double getBalanceCreditMilk() {
        return balanceCreditMilk;
    }

    public void setBalanceCreditMilk(double balanceCreditMilk) {
        this.balanceCreditMilk = balanceCreditMilk;
    }

    public double getBalanceLoan() {
        return balanceLoan;
    }

    public void setBalanceLoan(double balanceLoan) {
        this.balanceLoan = balanceLoan;
    }

    public double getBalanceOtherExpense() {
        return balanceOtherExpense;
    }

    public void setBalanceOtherExpense(double balanceOtherExpense) {
        this.balanceOtherExpense = balanceOtherExpense;
    }

    public double getBalanceDoctorVisitingFees() {
        return balanceDoctorVisitingFees;
    }

    public void setBalanceDoctorVisitingFees(double balanceDoctorVisitingFees) {
        this.balanceDoctorVisitingFees = balanceDoctorVisitingFees;
    }

    public double getBalanceExpense() {
        return balanceExpense;
    }

    public void setBalanceExpense(double balanceExpense) {
        this.balanceExpense = balanceExpense;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }
}
