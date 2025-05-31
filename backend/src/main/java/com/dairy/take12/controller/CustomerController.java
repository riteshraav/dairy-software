package com.dairy.take12.controller;

import com.dairy.take12.model.Customer;
import com.dairy.take12.model.RefreshToken;
import com.dairy.take12.model.SmsRequest;
import com.dairy.take12.repository.CustomerRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.service.AdminDetailsService;
import com.dairy.take12.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    SearchRepo searchRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @GetMapping("/getAll/{adminId}")
    public List<Customer> getAllCustomer(@PathVariable String adminId){
        return searchRepo.searchByAdmin(adminId);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomerAuth(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        String requestHeader = request.getHeader("Authorization");
        String accessToken = null;
        String id = null;

        if(requestHeader == null || !requestHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out header in null");
        }

        accessToken = requestHeader.substring(7);
        if(accessToken.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out refresh token is empty");
        }
        try{
            id  = jwtService.extractId(accessToken);
        }
        catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Access token expired\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out expired token");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid access token\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out jwt exception");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(id != null && authentication.isAuthenticated())
        {
            System.out.println("token is authorised");
            UserDetails adminDetails = context.getBean(AdminDetailsService.class).loadUserByUsername(id);
            if(jwtService.validateToken(accessToken,adminDetails))
            {
                List<Customer> customerList = searchRepo.searchByAdmin(id);
                System.out.println("custommer list size is "+customerList.size());
                customerList.forEach(c-> System.out.println(c.getName()));
                return  ResponseEntity.ok(customerList);
            }
        }
        System.out.println(id+"here is id");
        System.out.println(authentication.isAuthenticated()+"get anuthentication");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out unknown error");
    }
    @PostMapping("/add")
    public void addCustomer(@RequestBody Customer customer)
    {
        customer.setId();
        customerRepo.save(customer);
        sendSmsFromAdmin(customer.getAdminId(),customer,"Hello this is test messaging");
    }
    private void sendSmsFromAdmin(String adminPhone,Customer customer,String message)
    {
        String url = "http://"+adminPhone+":5000/send-sms";
        RestTemplate restTemplate = new RestTemplate();
        SmsRequest smsRequest = new SmsRequest(customer.getPhone(),message);
        restTemplate.postForEntity(url, smsRequest, String.class);
    }
    @PostMapping("/update")
    public void updateCustomer(@RequestBody Customer customer) {
        customerRepo.save(customer);
    }

    @GetMapping("/search/{cid}")
    public Customer searchCustomer(@PathVariable String cid)
    {
        return searchRepo.searchCustomer(cid);
    }

    @DeleteMapping("/delete/{adminId}")
    public void deleteCustomers(@PathVariable String adminId, @RequestBody List<String> customerCodeList){
        System.out.println("invoked");
        List<String> deleteIdList = new ArrayList<>();
        for(String customerId : customerCodeList)
        {
            System.out.println(adminId+"_"+customerId);
            deleteIdList.add(adminId+"_"+customerId);
        }
        customerRepo.deleteAllById(deleteIdList);
    }
}
