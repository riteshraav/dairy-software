package com.dairy.take12.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "ledger_data")
public class Ledger {
        String adminId;
        String customerID;
        double currentBalance;
        double totalBalance;
        double deduction;
        Date date;

        public String getAdminId() {
                return adminId;
        }

        public void setAdminId(String adminId) {
                this.adminId = adminId;
        }

        public String getCustomerID() {
                return customerID;
        }

        public void setCustomerID(String customerID) {
                this.customerID = customerID;
        }

        public double getCurrentBalance() {
                return currentBalance;
        }

        public void setCurrentBalance(double currentBalance) {
                this.currentBalance = currentBalance;
        }

        public double getTotalBalance() {
                return totalBalance;
        }

        public void setTotalBalance(double totalBalance) {
                this.totalBalance = totalBalance;
        }

        public double getDeduction() {
                return deduction;
        }

        public void setDeduction(double deduction) {
                this.deduction = deduction;
        }

        public Date getDate() {
                return date;
        }

        public void setDate(Date date) {
                this.date = date;
        }
}
