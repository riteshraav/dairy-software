package com.dairy.take12.repository;

import com.dairy.take12.model.CattleFeedPurchase;
import com.dairy.take12.model.CattleFeedSell;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CattleFeedSellRepo extends MongoRepository<CattleFeedSell,String> {
    List<CattleFeedSell> findAllByAdminId(String adminId);
}
