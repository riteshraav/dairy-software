package com.dairy.take12.controller;

import com.dairy.take12.model.CattleFeedSell;
import com.dairy.take12.model.CustomerBalance;
import com.dairy.take12.model.Ledger;
import com.dairy.take12.repository.CattleFeedSellRepo;
import com.dairy.take12.repository.CustomerBalanceRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.util.DateConverter;
import com.twilio.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cattleFeedSell")
public class CattleFeedSellController {
    @Autowired
    CattleFeedSellRepo cattleFeedSellRepo;
    @Autowired
    CustomerBalanceRepo customerBalanceRepo;
    @Autowired
    SearchRepo searchRepo;

    DateConverter dateConverter = new DateConverter();
    @PostMapping("/sell")
        public ResponseEntity<?> sellCattleFeed(@RequestBody CattleFeedSell cattleFeedSell) {
            cattleFeedSell.generateId();
            try{
                if(Objects.equals(cattleFeedSell.getModeOfPayback(), "Credit"))
                {
                    String _id = cattleFeedSell.getAdminId()+"_"+cattleFeedSell.getCustomerId();
                    CustomerBalance customerBalance = customerBalanceRepo.findById(_id).orElse(new CustomerBalance(_id));
                    double cattleFeedBalance = customerBalance.getBalanceCattleFeed();
                    CattleFeedSell temp = cattleFeedSellRepo.findById(cattleFeedSell.getId()).orElse(new CattleFeedSell());
                    if(temp.getId() != null)
                    {
                        System.out.println(temp.getId());
                        System.out.println("old edit");
                        cattleFeedBalance = cattleFeedBalance - temp.getTotalAmount();
                    }
                    System.out.println(cattleFeedBalance);
                    customerBalance.setBalanceCattleFeed(cattleFeedSell.getTotalAmount()+cattleFeedBalance);
                    customerBalanceRepo.save(customerBalance);

                }

                cattleFeedSellRepo.save(cattleFeedSell);
                return ResponseEntity.ok(cattleFeedSell);
            }
            catch (Exception e)
            {
                System.out.println("exception while adding cattlefeed sell "+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("exception while adding cattlefeed sell ");
            }
        }

        @GetMapping("/getAllForAdmin/{adminId}")
        public List<CattleFeedSell> getAllCattleFeedForAdmin(@PathVariable String adminId) {
                return cattleFeedSellRepo.findAllByAdminId(adminId);
        }
        @PostMapping("/getAllForCustomers/{adminId}/{fromDate}/{toDate}")
        public List<CattleFeedSell> getAllCattleFeedSell(@PathVariable String adminId, @PathVariable String fromDate, @PathVariable String toDate, @RequestBody List<String> customerIdList) throws ParseException {
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
            System.out.println("invoked");
            List<CattleFeedSell> cattleFeedSellList = new ArrayList<>();
            for(String customerId : customerIdList)
            {
                System.out.println(customerId);
                cattleFeedSellList.addAll(searchRepo.getAllCattleFeedSellForCustomer(adminId,customerId,from,to));
            }
            System.out.println("cattle feed sell list "+cattleFeedSellList.size());
        return cattleFeedSellList;
        }

        @PostMapping("/delete")
        public ResponseEntity editCattleFeedSell(@RequestBody CattleFeedSell cattleFeedSell){
            try{
                if(Objects.equals(cattleFeedSell.getModeOfPayback(), "Credit"))
                {
                    String _id = cattleFeedSell.getAdminId()+"_"+cattleFeedSell.getCustomerId();
                    CustomerBalance customerBalance = customerBalanceRepo.findById(_id).orElse(new CustomerBalance(_id));
                    double cattleFeedBalance = customerBalance.getBalanceCattleFeed();

                    System.out.println(cattleFeedBalance);
                    customerBalance.setBalanceCattleFeed(cattleFeedBalance - cattleFeedSell.getTotalAmount());
                    customerBalanceRepo.save(customerBalance);

                }

                cattleFeedSellRepo.deleteById(cattleFeedSell.getId());
                return ResponseEntity.ok(cattleFeedSell);
            }
            catch (Exception e)
            {
                System.out.println("exception while adding cattlefeed sell "+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("exception while adding cattlefeed sell ");
            }
        }
}
