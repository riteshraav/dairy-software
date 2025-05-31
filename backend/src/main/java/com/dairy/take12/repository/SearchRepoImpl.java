package com.dairy.take12.repository;

import com.dairy.take12.model.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;
import java.util.Arrays;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Component
public class SearchRepoImpl implements SearchRepo {

   @Autowired
   MongoClient mongoClient;

   @Autowired
    MongoConverter converter;

@Override
public Admin findByPhone(String text) {


    final List<Admin> admins = new ArrayList<>();
    MongoDatabase database = mongoClient.getDatabase("take12");
    MongoCollection<Document> collection = database.getCollection("admin");
    AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                    new Document("text",
                            new Document("query", text)
                                    .append("path", "_id")))));


    result.forEach(doc -> admins.add(converter.read(Admin.class,doc)));
    if(admins.isEmpty())
        return new Admin();
    return admins.get(0);
}

    @Override
    public Customer searchCustomer(String id) {

        final List<Customer> customers = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("customer");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                new Document("_id", id))));

        result.forEach(doc -> customers.add(converter.read(Customer.class,doc)));
        if(customers.isEmpty())
            return new Customer();
        return customers.get(0);
    }

    @Override
    public List<Customer> searchByAdmin(String adminId) {
        final List<Customer> customers = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("customer");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId", adminId)),
                              new Document("$sort",
                             new Document("cid", 1L))));

        result.forEach(doc -> customers.add(converter.read(Customer.class,doc)));
        if(customers.isEmpty())
            return customers;
        return customers;
    }

    @Override
    public List<MilkCollection> searchByCustomer(String customerId, String adminId) {
        final List<MilkCollection> milkCollections = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("collection1");

        // Match both customerId and adminId
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                        new Document("adminId", adminId)
                                .append("customerId", customerId))));

        result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class, doc)));

        return milkCollections;
    }

    public List<MilkCollection> findByMilkTypeAndDateBetween(String milkType, String fromDate, String toDate, String customerId,String adminId) {
        final List<MilkCollection> milkCollections = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("collection1");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match", new Document("adminId", adminId)  // Add adminId filter
                                .append("customerId", customerId)
                                .append("milkType", milkType)
                                .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("date", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class,doc)));

        return milkCollections;
    }
    @Override
    public List<MilkCollection> findByMilkTypeAndDateBetween(String milkType, Date fromDate, Date toDate,String adminId) {
        final List<MilkCollection> milkCollections = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("collection1");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match", new Document("adminId", adminId)  // Add adminId filter
                                .append("milkType", milkType)
                                .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("customerId", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class,doc)));
        return milkCollections;
    }

    @Override
    public List<MilkCollection> getMilkCollectionsForCustomersByDateRangeAndType(String milkType, Date fromDate, Date toDate, String customerId, String adminId) {
                List<MilkCollection> milkCollections =     new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("collection1");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match", new Document("adminId", adminId)
                                .append("customerId",customerId)
                                .append("milkType", milkType)
                                .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("customerId", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class,doc)));
        return milkCollections;

    }


    @Override
    public List<MilkCollection> generateReport(String adminId) {

        final List<MilkCollection> milkCollections = new ArrayList<>();
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "collection1";


            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            AggregateIterable<Document> result = collection.aggregate(
                    Arrays.asList(
                            new Document("$match",
                                    new Document("adminId", adminId)
                    ),
                            new Document("$sort", new Document("date", 1))
                    ));

            result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class, doc)));

            if (milkCollections.isEmpty()) {
                return milkCollections; // Return empty list instead of null
            }


        return milkCollections;
    }

    public List<MilkCollection> getCollectionForAdminForFromTo(String adminId, Date from, Date to) {

        final List<MilkCollection> milkCollections = new ArrayList<>();
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "collection1";


        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$match",
                                new Document("adminId", adminId)
                                        .append("date", new Document("$gte", from).append("$lte", to))
                        ),
                        new Document("$sort", new Document("date", 1))
                ));

        result.forEach(doc -> milkCollections.add(converter.read(MilkCollection.class, doc)));

        return milkCollections;
    }

    @Override
    public List<CattleFeedSell> getAllCattleFeedSellForAdmin(String adminId) {

        final List<CattleFeedSell> cattleFeedSells = new ArrayList<>();
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "cattleFeed_sell";


        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$match",
                                new Document("customerId", new Document("$regex", "^" + adminId +"[^0]"))
                        ),
                        new Document("$sort", new Document("date", 1))
                ));

        result.forEach(doc -> cattleFeedSells.add(converter.read(CattleFeedSell.class, doc)));

        if (cattleFeedSells.isEmpty()) {
            return cattleFeedSells; // Return empty list instead of null
        }


        return cattleFeedSells;
    }


    @Override
    public List<CattleFeedSell> getAllCattleFeedSellForCustomer(String adminId, String customerId, Date from, Date to) {
        final List<CattleFeedSell> cattleFeedSells = new ArrayList<>();
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "cattleFeed_sell";
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$match", new Document("adminId", adminId)
                                .append("customerId", customerId)
                                .append("date", new Document("$gte", from).append("$lte", to))
                        ),
                        new Document("$sort", new Document("date", 1))
                )
        );

        result.forEach(doc -> cattleFeedSells.add(converter.read(CattleFeedSell.class, doc)));

        return cattleFeedSells;
    }


    @Override
    public List<CattleFeedPurchase> getAllCattleFeedPurchase(String adminId) {
        final List<CattleFeedPurchase> cattleFeedPurchase = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("cattleFeedStock");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId",adminId)),
                        new Document("$sort",
                                new Document("code", 1L))));

        result.forEach(doc -> cattleFeedPurchase.add(converter.read(CattleFeedPurchase.class,doc)));
        return cattleFeedPurchase;
    }

    @Override
    public CustomerBalance getCustomerBalance(String adminId, String customerId) {
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "customer_balance";
        List<CustomerBalance> customerBalancesList = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document matchStage = new Document("$match", new Document("adminId", adminId).append("customerId", customerId));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(matchStage));

        result.forEach(doc -> customerBalancesList.add(converter.read(CustomerBalance.class,doc)));
        return customerBalancesList.get(0);

    }

    @Override
    public List<Deduction> findDeduction(String adminId, String customerId) {
        final String DATABASE_NAME = "take12";
        final String COLLECTION_NAME = "deduction";
        List<Deduction> deductionList = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Document matchStage = new Document("$match", new Document("adminId", adminId).append("customerId", customerId));

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(matchStage));

        result.forEach(doc -> deductionList.add(converter.read(Deduction.class,doc)));
       return deductionList;

    }

    @Override
    public List<Deduction> findDeduction(String adminId, String customerId, Date fromDate, Date toDate) {
        final List<Deduction> deductionList = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("deduction");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId", adminId)
                                        .append("customerId", customerId)
                                        .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("date", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> deductionList.add(converter.read(Deduction.class,doc)));

        return deductionList;
    }

    @Override
    public List<Deduction> findDeduction(String adminId, Date fromDate, Date toDate) {
        final List<Deduction> deductionList = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("deduction");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId", adminId)
                                        .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("date", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> deductionList.add(converter.read(Deduction.class,doc)));

        return deductionList;
    }

    @Override
    public List<Ledger> findLedgerForCustomer(String adminId, String customerID, Date fromDate, Date toDate) {
        final List<Ledger> ledgerList = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("ledger_data");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId", adminId)
                                        .append("customerId",customerID)
                                        .append("date", new Document("$gte", fromDate).append("$lte", toDate))),
                        new Document("$sort", new Document("date", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> ledgerList.add(converter.read(Ledger.class,doc)));

        return ledgerList;

    }

    @Override
    public List<CattleFeedSupplier> getAllCattleFeedSupplierForAdmin(String adminId) {
        final List<CattleFeedSupplier> cattleFeedSupplierList = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("take12");
        MongoCollection<Document> collection = database.getCollection("CattleFeedSupplier");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(new Document("$match",
                                new Document("adminId", adminId)),
                        new Document("$sort", new Document("date", 1)) // 1 for ascending order
                )
        );

        result.forEach(doc -> cattleFeedSupplierList.add(converter.read(CattleFeedSupplier.class,doc)));

        return cattleFeedSupplierList;
    }


}
