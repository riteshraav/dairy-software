package com.dairy.take12.controller;

import com.dairy.take12.model.LoanEntry;
import com.dairy.take12.model.CustomerBalance;
import com.dairy.take12.repository.CustomerBalanceRepo;
import com.dairy.take12.repository.LoanEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("loanEntry")
@CrossOrigin(origins = "*")

public class LoanEntryController {
    @Autowired
    LoanEntryRepo loanEntryRepo;
    @Autowired
    CustomerBalanceRepo customerBalanceRepo;
    @PostMapping("/add")
    public void addLoanEntry(@RequestBody LoanEntry loanEntry)
    {
        loanEntry.set_id();
        if(Objects.equals(loanEntry.getModeOfPayback(), "Credit"))
        {
            String _id =loanEntry.get_id();
            CustomerBalance customerBalance = customerBalanceRepo.findById(_id).orElse(new CustomerBalance(_id));
            double balanceLoan = customerBalance.getBalanceLoan();
            LoanEntry temp = loanEntryRepo.findById(_id).orElse(new LoanEntry());
            if(temp.get_id() != null)
            {
                System.out.println(temp.get_id());
                System.out.println("old edit");
                balanceLoan = balanceLoan - temp.getLoanAmount();
            }
            System.out.println(balanceLoan);
            customerBalance.setBalanceLoan(loanEntry.getLoanAmount()+balanceLoan);
            customerBalanceRepo.save(customerBalance);

        }
        loanEntryRepo.save(loanEntry);
    }
    @GetMapping("/getAllLoanEntryForAdmin/{adminId}")
    public ResponseEntity<?> getAllLoanEntryForAdmin(@PathVariable String adminId)
    {

        List<LoanEntry> loanEntryList = loanEntryRepo.findAllByAdminId(adminId);
        System.out.println("loan entry  size is "+loanEntryList.size());
        return ResponseEntity.ok(loanEntryList);
    }
    @GetMapping("/getForCustomer/{adminId}/{customerId}")
    public ResponseEntity<?> getForCustomer(@PathVariable String adminId, @PathVariable String customerId)
    {

        Optional<LoanEntry> loanEntry = loanEntryRepo.findById(adminId+"_"+customerId);
        if(loanEntry.isPresent())
        {
            System.out.println("loan entry  is "+loanEntry);
            return ResponseEntity.ok(loanEntry);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find the body");

    }

    @DeleteMapping("/delete")
    public void deleteEntry(@RequestBody LoanEntry loanEntry)
    {
        loanEntry.set_id();
        if(Objects.equals(loanEntry.getModeOfPayback(), "Credit"))
        {
            String _id =loanEntry.get_id();
            CustomerBalance customerBalance = customerBalanceRepo.findById(_id).orElse(new CustomerBalance(_id));
            customerBalance.setBalanceLoan(0);
            customerBalanceRepo.save(customerBalance);
        }
        loanEntryRepo.deleteById(loanEntry.get_id());
    }
}
