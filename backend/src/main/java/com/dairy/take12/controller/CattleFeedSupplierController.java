package com.dairy.take12.controller;

import com.dairy.take12.model.CattleFeedSupplier;
import com.dairy.take12.repository.CattleFeedSupplierRepo;
import com.dairy.take12.repository.SearchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cattleFeedSupplier")
@CrossOrigin(origins = "*")
public class CattleFeedSupplierController {
    @Autowired
    CattleFeedSupplierRepo cattleFeedSupplierRepo;
    @Autowired
    SearchRepo searchRepo;
    @PostMapping("/add")
    public void addCattleFeedSupplier(@RequestBody CattleFeedSupplier cattleFeedSupplier) {
        cattleFeedSupplierRepo.save(cattleFeedSupplier);

    }
    @GetMapping("/getAll/{adminId}")
    public List<CattleFeedSupplier> getAllCattleFeedSuppliers(@PathVariable String adminId) {
        List<CattleFeedSupplier> cattleFeedSuppliers = cattleFeedSupplierRepo.findAllByAdminId(adminId);
        System.out.println(cattleFeedSuppliers.size() + " cattlefeed upplier size");
        return cattleFeedSuppliers;

    }
}
