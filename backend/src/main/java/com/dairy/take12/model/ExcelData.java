package com.dairy.take12.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExcelData {
    Date date;
    String note;
    List<List<String>> rateData;
}
