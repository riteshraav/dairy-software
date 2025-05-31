package com.dairy.take12.controller;

import com.dairy.take12.model.Ledger;
import com.dairy.take12.repository.LedgerRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ledger")
public class LedgerController {

    @Autowired
    LedgerRepo ledgerRepo;

    @Autowired
    SearchRepo searchRepo;

    DateConverter dateConverter = new DateConverter();
    @PostMapping("/add")
    public void addLedger(@RequestBody Ledger ledger)
    {
        ledgerRepo.save(ledger);
    }

    @PostMapping("/getForCustomers/{adminID}/{fromDate}/{toDate}")
    public List<Ledger> getLedgerForCustomers(@PathVariable String adminId, @PathVariable String fromDate, @PathVariable String toDate, @RequestBody List<String> customerIdList) throws ParseException {
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        List<Ledger> ledgerList = new ArrayList<>();
        for(String customerId : customerIdList)
        {
                ledgerList.addAll(searchRepo.findLedgerForCustomer(adminId,customerId,from,to));
        }
        return ledgerList;
    }
}
