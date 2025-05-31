package com.dairy.take12.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@Data
@Document(collection = "rate_data")
public class RateData {
    @Id
    private String adminId;
    private List<ExcelData> excelData;
    // Constructors, getters, setters

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public List<ExcelData> getExcelData() {
        return excelData;
    }

    public void setExcelData(List<ExcelData> excelData) {
        this.excelData = excelData;
    }
}
