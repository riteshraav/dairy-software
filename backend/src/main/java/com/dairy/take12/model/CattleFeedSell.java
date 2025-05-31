package com.dairy.take12.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "cattleFeed_sell")
@Getter
@Setter
public class CattleFeedSell {
    @Id
    String id;
    String customerId;
    String feedName;
    int quantity;
    double rate;
    double totalAmount;
    Date date;
    String modeOfPayback;
    String adminId;
    double totalCattleFeedBalance;
    double deduction;

    public CattleFeedSell() {
    }

    public CattleFeedSell(String id, String customerId, String feedName, int quantity, double rate, double totalAmount, Date date, String modeOfPayback, String adminId, double totalCattleFeedBalance, double deduction) {
        this.id = id;
        this.customerId = customerId;
        this.feedName = feedName;
        this.quantity = quantity;
        this.rate = rate;
        this.totalAmount = totalAmount;
        this.date = date;
        this.modeOfPayback = modeOfPayback;
        this.adminId = adminId;
        this.totalCattleFeedBalance = totalCattleFeedBalance;
        this.deduction = deduction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getModeOfPayback() {
        return modeOfPayback;
    }

    public void setModeOfPayback(String modeOfPayback) {
        this.modeOfPayback = modeOfPayback;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public double getTotalCattleFeedBalance() {
        return totalCattleFeedBalance;
    }

    public void setTotalCattleFeedBalance(double totalCattleFeedBalance) {
        this.totalCattleFeedBalance = totalCattleFeedBalance;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public void generateId() {
        this.id = adminId + "_" + date.getTime(); // Use timestamp to make it unique
    }

}
