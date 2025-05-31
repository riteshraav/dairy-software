package com.dairy.take12.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "collection1")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilkCollection {
        String adminId;
        String customerId;
        double quantity;
        double fat;

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

        public double getQuantity() {
                return quantity;
        }

        public void setQuantity(double quantity) {
                this.quantity = quantity;
        }

        public double getFat() {
                return fat;
        }

        public void setFat(double fat) {
                this.fat = fat;
        }

        public double getSnf() {
                return snf;
        }

        public void setSnf(double snf) {
                this.snf = snf;
        }

        public double getRate() {
                return rate;
        }

        public void setRate(double rate) {
                this.rate = rate;
        }

        public double getTotalValue() {
                return totalValue;
        }

        public void setTotalValue(double totalValue) {
                this.totalValue = totalValue;
        }

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

        public String getMilkType() {
                return milkType;
        }

        public void setMilkType(String milkType) {
                this.milkType = milkType;
        }

        public Date getDate() {
                return date;
        }

        public void setDate(Date date) {
                this.date = date;
        }

        double snf;
        double rate;
        double totalValue;
        String time;
        String milkType;
        Date date;
}
