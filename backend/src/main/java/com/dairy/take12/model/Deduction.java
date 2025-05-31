package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "deduction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deduction {
    @Id
            String _id;
    String adminId;
    String customerId;
    double CattleFeed;
    double Advance;
    double CreditMilk;
    double Loan;
    double OtherExpense;
    double DoctorVisitFees;
    double Expense;
    Date date;
    double Total;
    double totalCattleFeedBalance;

    public void generateId(){
        this._id = this.adminId + "_"+this.date.getTime();
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

    public double getCattleFeed() {
        return CattleFeed;
    }

    public void setCattleFeed(double cattleFeed) {
        CattleFeed = cattleFeed;
    }

    public double getAdvance() {
        return Advance;
    }

    public void setAdvance(double advance) {
        Advance = advance;
    }

    public double getCreditMilk() {
        return CreditMilk;
    }

    public void setCreditMilk(double creditMilk) {
        CreditMilk = creditMilk;
    }

    public double getLoan() {
        return Loan;
    }

    public void setLoan(double loan) {
        Loan = loan;
    }

    public double getOtherExpense() {
        return OtherExpense;
    }

    public void setOtherExpense(double otherExpense) {
        OtherExpense = otherExpense;
    }

    public double getDoctorVisitFees() {
        return DoctorVisitFees;
    }

    public void setDoctorVisitFees(double doctorVisitFees) {
        DoctorVisitFees = doctorVisitFees;
    }

    public double getExpense() {
        return Expense;
    }

    public void setExpense(double expense) {
        Expense = expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getTotalCattleFeedBalance() {
        return totalCattleFeedBalance;
    }

    public void setTotalCattleFeedBalance(double totalCattleFeedBalance) {
        this.totalCattleFeedBalance = totalCattleFeedBalance;
    }
}