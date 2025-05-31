package com.dairy.take12.repository;

import com.dairy.take12.model.Deduction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeductionRepo extends MongoRepository<Deduction,String> {
}
