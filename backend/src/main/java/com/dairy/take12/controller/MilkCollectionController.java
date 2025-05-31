package com.dairy.take12.controller;
import com.dairy.take12.model.MilkCollection;
import com.dairy.take12.repository.MilkCollectionRepo;
import com.dairy.take12.repository.SearchRepoImpl;
import com.dairy.take12.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/collection")
@CrossOrigin(origins = "*")
public class MilkCollectionController {
    @Value("${spring.data.mongodb.uri}")
    String mongourl;
    @Autowired
    MilkCollectionRepo milkCollectionRepo;
    @Autowired
    private SearchRepoImpl searchRepoImpl;

    DateConverter dateConverter = new DateConverter();

    @GetMapping("/getAll")
    public List<MilkCollection> getAll(){
        return milkCollectionRepo.findAll();
    }
    @PostMapping("/add")
    public void addCollection(@RequestBody MilkCollection milkCollection) {


        milkCollectionRepo.save(milkCollection);

    }
    @GetMapping("/getAllForCustomer/{customerId}/{adminId}")
    public List<MilkCollection> getAllCollection(@PathVariable String customerId,@PathVariable String adminId){
      return  searchRepoImpl.searchByCustomer(customerId,adminId);
    }

    @GetMapping("/filter/{fromDate}/{toDate}/{milkType}/{customerId}/{adminId}")
    public List<MilkCollection> getMilkCollectionsByDateRangeAndType (@PathVariable String fromDate,@PathVariable String toDate,@PathVariable String milkType, @PathVariable String customerId, @PathVariable String adminId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return searchRepoImpl.findByMilkTypeAndDateBetween(milkType,fromDate,toDate,customerId,adminId);
    }
    @GetMapping("/filter/{fromDate}/{toDate}/{milkType}/{adminId}")
    public List<MilkCollection> getMilkCollectionsByDateRangeAndType (@PathVariable String fromDate,@PathVariable String toDate,@PathVariable String milkType,  @PathVariable String adminId) throws ParseException {
        System.out.println(mongourl);

        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(milkType);
        System.out.println(adminId);
        List<MilkCollection> milkCollectionList = searchRepoImpl.findByMilkTypeAndDateBetween(milkType,from,to,adminId);
        System.out.println("milkcollection list size is : "+milkCollectionList.size());
        return milkCollectionList ;
    }
    @PostMapping("/filterForCustomers/{fromDate}/{toDate}/{cow}/{buffalo}/{adminId}")
    public List<MilkCollection> getMilkCollectionsForCustomersByDateRangeAndType (@RequestBody List<String> customerList,@PathVariable String fromDate,@PathVariable String toDate,@PathVariable boolean cow,@PathVariable boolean buffalo,  @PathVariable String adminId) throws ParseException {
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        System.out.println("From Date: " + from);
        System.out.println("To Date: " + to);
        System.out.println("Admin ID: " + adminId);
        System.out.println("Customer List: " + customerList);
        System.out.println("Buffalo: " + buffalo + ", Cow: " + cow);

        List<MilkCollection> milkCollectionList = new ArrayList<>();
        if(buffalo)
           for(String c : customerList)
           {
               milkCollectionList.addAll(searchRepoImpl.getMilkCollectionsForCustomersByDateRangeAndType("buffalo",from,to,c,adminId));

           }
        if (cow)
            for(String c : customerList)
            {

                milkCollectionList.addAll(searchRepoImpl.getMilkCollectionsForCustomersByDateRangeAndType("cow",from,to,c,adminId));

            }
      return milkCollectionList;

    }
    @GetMapping("/getAllForAdmin/{adminId}")
    public List<MilkCollection> generateReport (@PathVariable String adminId)  {

        return searchRepoImpl.generateReport(adminId);
    }
    @GetMapping("/getAllForAdmin/{adminId}/{fromDate}/{toDate}")
    public List<MilkCollection> getAllForAdminFromDateToToDate (@PathVariable String adminId, @PathVariable String fromDate, @PathVariable String toDate) throws ParseException {
        Date from = dateConverter.convertToDate(fromDate);
        Date to = dateConverter.convertToDate(toDate);
        return searchRepoImpl.getCollectionForAdminForFromTo(adminId,from,to);
    }
}
