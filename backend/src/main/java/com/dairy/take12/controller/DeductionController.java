package com.dairy.take12.controller;

import com.dairy.take12.model.CattleFeedSell;
import com.dairy.take12.model.CustomerAdvance;
import com.dairy.take12.model.Deduction;
import com.dairy.take12.model.LoanEntry;
import com.dairy.take12.repository.CustomerAdvanceRepo;
import com.dairy.take12.repository.DeductionRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.service.JwtService;
import com.dairy.take12.util.DateConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping("/deduction")
public class DeductionController {
    @Autowired
    DeductionRepo deductionRepo;
    @Autowired
    SearchRepo searchRepo;
    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;
    DateConverter dateConverter = new DateConverter();

    @Autowired
    private CattleFeedSellController cattleFeedSellController;
    @Autowired
    private CustomerAdvanceController customerAdvanceController;

    @Autowired
    private LoanEntryController loanEntryController;
    @PostMapping("/add")
    public void addDeduction(@RequestBody Deduction deduction){
        System.out.println("deduction updated");
        deduction.generateId();
        String cattleFeedSellId = deduction.getAdminId() + "_" +deduction.getCustomerId();
        CattleFeedSell cattleFeedSell = new CattleFeedSell(cattleFeedSellId,deduction.getCustomerId(),"",0,0,0,deduction.getDate(),"Deduction",deduction.getAdminId(),deduction.getTotalCattleFeedBalance(),deduction.getTotal());
        cattleFeedSellController.sellCattleFeed(cattleFeedSell);
        deductionRepo.save(deduction);
    }

    @GetMapping("/get/{adminId}/{customerId}")
    public List<Deduction> getDeduction(@PathVariable String adminId, @PathVariable String customerId)
    {
        return searchRepo.findDeduction(adminId,customerId);
    }
    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getDeductionAuth(@PathVariable String customerId, HttpServletRequest request) throws IOException {
        String adminId = extractId(request);
            return ResponseEntity.ok(searchRepo.findDeduction(adminId,customerId));
    }
    @GetMapping("/get/{adminId}/{customerId}/{fromDate}/{toDate}")
    public List<Deduction> getDeduction(@PathVariable String adminId, @PathVariable String customerId, @PathVariable String fromDate, @PathVariable String toDate) throws ParseException {
        System.out.println(fromDate);
        System.out.println(toDate);
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        return searchRepo.findDeduction(adminId,customerId,from,to);
    }
    @GetMapping("/get/{customerId}/{fromDate}/{toDate}")
    public ResponseEntity<?> getDeduction(@PathVariable String customerId, @PathVariable String fromDate, @PathVariable String toDate,HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String adminId = extractId(request);

            System.out.println(fromDate);
            System.out.println(toDate);
            Date from = dateConverter.convertToDate(fromDate);
            Date to = dateConverter.convertToDate(toDate);
            return ResponseEntity.ok(searchRepo.findDeduction(adminId,customerId,from,to));
    }
    @PostMapping("/get/{adminId}/{fromDate}/{toDate}")
    public List<Deduction> getDeduction(@PathVariable String adminId,  @PathVariable String fromDate, @PathVariable String toDate,@RequestBody List<String>customerCodeList) throws ParseException {
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        List<Deduction> list = new ArrayList<>();

        for(String customerCode: customerCodeList)
        {
            list.addAll(searchRepo.findDeduction(adminId,customerCode,from,to)) ;
        }
        System.out.println(list);
        System.out.println(list.size());
        return list;
    }
    @PostMapping("/get/{fromDate}/{toDate}")
    public ResponseEntity<?> getDeductionAuth( @PathVariable String fromDate, @PathVariable String toDate,@RequestBody List<String> customerCodeList, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String adminId = extractId(request);
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);

        List<Deduction> list = new ArrayList<>();
            for (String customerCode : customerCodeList) {
                list.addAll(searchRepo.findDeduction(adminId, customerCode, from, to));
            }
            System.out.println(list);
            System.out.println("list of found deductoin is : "+list.size());
            return ResponseEntity.ok(list);

    }
    @PostMapping("/getForAdmin/{fromDate}/{toDate}")
    public ResponseEntity<?> getDeductionAuth(@PathVariable String fromDate, @PathVariable String toDate, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String adminId = extractId(request);
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        List<Deduction> list = new ArrayList<>();

           list = searchRepo.findDeduction(adminId, from, to) ;
            System.out.println(list);
            System.out.println(list.size());
            return ResponseEntity.ok(list);
        }

    @DeleteMapping("/delete/{adminId}/{date}")
    public void deleteDeduction(@PathVariable String adminId, @PathVariable String date) throws ParseException {
        String _id = adminId+"_"+ dateConverter.convertToDate(date);
        deductionRepo.deleteById(_id);
    }
    @DeleteMapping("/delete/{date}")
    public void deleteDeductionAuth( @PathVariable String date , HttpServletRequest request) throws ParseException {
        String adminId = extractId(request);
        String _id = adminId+"_"+ dateConverter.convertToDate(date);
        deductionRepo.deleteById(_id);
    }

    String extractId(HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");
        String accessToken = null;
        String adminId = null;
        accessToken = requestHeader.substring(7);
            adminId  = jwtService.extractId(accessToken);
            return  adminId;

    }
}
