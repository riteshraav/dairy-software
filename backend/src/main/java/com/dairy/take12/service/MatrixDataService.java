package com.dairy.take12.service;

import com.dairy.take12.model.ExcelData;
import com.dairy.take12.model.RateData;
import com.dairy.take12.repository.MatrixDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatrixDataService {
    private final MatrixDataRepo repository;

    public MatrixDataService(MatrixDataRepo repository) {
        this.repository = repository;
    }

    public RateData getUserMatrixData(String adminId) {
        Optional<RateData> rateData = repository.findById(adminId);
        if(rateData.isPresent())
        {
            return rateData.get();
        }
        RateData rateData1 = new RateData();
        rateData1.setAdminId(adminId);
        return rateData1;
    }

    public RateData saveUserMatrixData(RateData rateData) {
        return repository.save(rateData);
    }

}
