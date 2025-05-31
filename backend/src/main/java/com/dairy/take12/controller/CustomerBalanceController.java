package com.dairy.take12.controller;


import com.dairy.take12.model.Customer;
import com.dairy.take12.model.CustomerBalance;
import com.dairy.take12.repository.CustomerBalanceRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customerBalance")
@CrossOrigin(origins = "*")

public class CustomerBalanceController {
    @Autowired
    CustomerBalanceRepo customerBalanceRepo;

    @Autowired
    SearchRepo searchRepo;
    @Autowired
    JwtService jwtService;
    @PostMapping("/add")
    public void addCustomerBalance(@RequestBody CustomerBalance customerBalance)
    {
        customerBalance.set_id(customerBalance.getAdminId() +"_"+customerBalance.getCustomerId());
        customerBalanceRepo.save(customerBalance);
    }
    @GetMapping("/get/{id}")
    public CustomerBalance getCustomerBalance(@PathVariable String id)
    {
        System.out.println("invoked");

        CustomerBalance c = customerBalanceRepo.findById(id).orElse(new CustomerBalance(id));
        System.out.println("this is customerbalance"+c.getBalanceCattleFeed());
        return c;

    }
    @PostMapping("/getForCustomers/{adminId}")
    public List<CustomerBalance> getCustomerBalance(@PathVariable String adminId,@RequestBody List<String> customerList)
    {
        List<CustomerBalance> customerBalanceList = new ArrayList<>();
        List<String> customerBalanceIdList = new ArrayList<>();
        for(String id : customerList)
        {
            customerBalanceIdList.add(adminId +"_"+id);
        }

        return customerBalanceRepo.findAllById(customerBalanceIdList);

    }  @PostMapping("/getForCustomers")
    public List<CustomerBalance> getCustomerBalanceAuth(@RequestBody List<String> customerList,HttpServletRequest request)
    {
        String adminId = jwtService.extractIdFromRequest(request);
        List<CustomerBalance> customerBalanceList = new ArrayList<>();
        List<String> customerBalanceIdList = new ArrayList<>();
        for(String id : customerList)
        {
            customerBalanceIdList.add(adminId +"_"+id);
        }

        return customerBalanceRepo.findAllById(customerBalanceIdList);

    }



}
