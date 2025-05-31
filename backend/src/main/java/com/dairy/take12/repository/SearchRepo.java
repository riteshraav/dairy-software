package com.dairy.take12.repository;

import com.dairy.take12.model.*;

import java.util.Date;
import java.util.List;

public interface SearchRepo {
    Admin findByPhone(String text);
    Customer searchCustomer( String cid);
    List<Customer> searchByAdmin(String adminId);
    List<MilkCollection>searchByCustomer(String customerId, String adminId);

    List<MilkCollection> findByMilkTypeAndDateBetween(String milkType, Date fromDate, Date toDate,String adminId);
    List<MilkCollection> getMilkCollectionsForCustomersByDateRangeAndType(String milkTypeList, Date fromDate, Date toDate,String customerId,String adminId);
    List<MilkCollection> generateReport(String adminId);
    List<CattleFeedSell> getAllCattleFeedSellForAdmin(String adminId);
    List<CattleFeedSell> getAllCattleFeedSellForCustomer(String adminId,String customerId,Date from,Date to);
    List<CattleFeedPurchase> getAllCattleFeedPurchase(String adminId);
    CustomerBalance getCustomerBalance(String adminId,String customerId);
    List<Deduction> findDeduction(String adminId,String customerId);
    List<Deduction> findDeduction(String adminId,String customerId,Date fromDate,Date toDate);
    List<Deduction> findDeduction(String adminId,Date fromDate,Date toDate);
    List<Ledger> findLedgerForCustomer(String adminId,String customerID,Date fromDate,Date  toDate);
    List<CattleFeedSupplier> getAllCattleFeedSupplierForAdmin(String adminId);
}
