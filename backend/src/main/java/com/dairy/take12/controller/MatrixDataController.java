package com.dairy.take12.controller;

import com.dairy.take12.model.RateData;
import com.dairy.take12.service.MatrixDataService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matrix")
public class MatrixDataController {
    private final MatrixDataService service;

    public MatrixDataController(MatrixDataService service) {
        this.service = service;
    }

    @GetMapping("/get/{adminId}")
    public ResponseEntity<RateData> getMatrix(
            @PathVariable String adminId
        )  {
        RateData rateData = service.getUserMatrixData(adminId);
        return ResponseEntity.ok(rateData);
    }

    @PostMapping("/add/{adminId}")
    public ResponseEntity<RateData> createMatrix(
            @PathVariable String adminId,
            @RequestBody RateData rateData) {
        RateData saved = service.saveUserMatrixData(rateData);
        return ResponseEntity.ok(saved);
    }

}