package com.dairy.take12.repository;

import com.dairy.take12.model.CattleFeedPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CattleFeedStockRepo extends MongoRepository<CattleFeedPurchase, String> {

}
