package com.dairy.take12.repository;

import com.dairy.take12.model.LoanEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LoanEntryRepo extends MongoRepository<LoanEntry,String> {
    List<LoanEntry> findAllByAdminId(String adminId);

}
