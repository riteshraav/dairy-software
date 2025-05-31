package com.dairy.take12.repository;

import com.dairy.take12.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepo extends MongoRepository<Admin, String> {

}
