package com.dairy.take12.repository;

import com.dairy.take12.model.RateData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixDataRepo extends MongoRepository<RateData, String> {
    RateData findByAdminId(String adminId);
}
