package com.dairy.take12.controller;

import com.dairy.take12.model.CustomerAdvance;
import com.dairy.take12.model.CustomerBalance;
import com.dairy.take12.model.LoanEntry;
import com.dairy.take12.repository.CustomerAdvanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/customerAdvance")
@CrossOrigin(origins = "*")

public class CustomerAdvanceController {
    @Autowired
    CustomerAdvanceRepo customerAdvanceRepo;

    @Autowired
    CustomerBalanceController customerBalanceController;

    @PostMapping("/add")
    public void addCustomerAdvance(@RequestBody CustomerAdvance customerAdvance) {
        System.out.println("customer advanced added");
        customerAdvance.setId();
        CustomerBalance customerBalance =  customerBalanceController.getCustomerBalance(customerAdvance.getAdminId()+"_"+customerAdvance.getCode());
        customerBalance.setBalanceAdvance(customerAdvance.getAdvanceAmount());
        customerBalanceController.addCustomerBalance(customerBalance);
        customerAdvanceRepo.save(customerAdvance);
    }
    @GetMapping("/getAllAdvanceForAdmin/{adminId}")
    public ResponseEntity<?> getAllAdvanceForAdmin(@PathVariable String adminId)
    {

        List<CustomerAdvance> advanceEntryList = customerAdvanceRepo.findAllByAdminId(adminId);
        System.out.println("advance entry  size is "+advanceEntryList.size());
        return ResponseEntity.ok(advanceEntryList);
    }
    @GetMapping("/getForCustomer/{adminId}/{customerId}")
    public ResponseEntity getForCustomer(@PathVariable String adminId,@PathVariable String customerId)
    {
        Optional<CustomerAdvance> advanceEntry = customerAdvanceRepo.findById(adminId+"_"+customerId);
        if(advanceEntry.isPresent())
        {
            System.out.println("advance entry  size is "+advanceEntry);

            return ResponseEntity.ok(advanceEntry);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("customer advance not founnd");
    }
    @DeleteMapping("/delete")
    public void deleteCustomerAdvance(@RequestBody CustomerAdvance customerAdvance) {
        customerAdvance.setId();
        System.out.println(customerAdvance.getId());
        CustomerBalance customerBalance =  customerBalanceController.getCustomerBalance(customerAdvance.getAdminId()+"_"+customerAdvance.getCode());
        customerBalance.setBalanceAdvance(0);
        customerBalanceController.addCustomerBalance(customerBalance);
        customerAdvanceRepo.deleteById(customerAdvance.getId());
    }
}
