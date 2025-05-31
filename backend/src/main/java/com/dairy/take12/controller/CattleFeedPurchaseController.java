package com.dairy.take12.controller;

import com.dairy.take12.model.CattleFeedPurchase;
import com.dairy.take12.model.CattleFeedSupplier;
import com.dairy.take12.repository.CattleFeedStockRepo;
import com.dairy.take12.repository.SearchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cattleFeedPurchase")
@CrossOrigin("*")
public class CattleFeedPurchaseController {
    @Autowired
    CattleFeedStockRepo cattleFeedStockRepo;
    @Autowired
    SearchRepo searchRepo;
    @PostMapping("/add")
    public ResponseEntity addCattleFeedPurchase(@RequestBody CattleFeedPurchase cattleFeedPurchase) {
        cattleFeedPurchase.generateId();
        cattleFeedStockRepo.save(cattleFeedPurchase);
        System.out.println("purchase make of cattle feed");
        return  ResponseEntity.ok(cattleFeedPurchase.getCode());
    }
    @GetMapping("/getAll/{adminId}")
    public List<CattleFeedPurchase> getAllCattleFeedSuppliers(@PathVariable String adminId) {
        return searchRepo.getAllCattleFeedPurchase(adminId);
    }
    @PostMapping("/delete")
    public ResponseEntity deleteCattleFeedPurchase(@RequestBody CattleFeedPurchase cattleFeedPurchase){
        cattleFeedStockRepo.deleteById(cattleFeedPurchase.getCode());
        return ResponseEntity.ok("cattle feed purchase deleted");
    }
}
