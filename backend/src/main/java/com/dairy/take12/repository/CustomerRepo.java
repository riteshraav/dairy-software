package com.dairy.take12.repository;

import com.dairy.take12.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepo extends MongoRepository<Customer,String> {

}
