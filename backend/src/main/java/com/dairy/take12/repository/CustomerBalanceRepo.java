package com.dairy.take12.repository;

import com.dairy.take12.model.CustomerBalance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerBalanceRepo extends MongoRepository<CustomerBalance,String> {
}
