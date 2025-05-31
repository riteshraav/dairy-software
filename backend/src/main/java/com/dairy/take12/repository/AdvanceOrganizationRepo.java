package com.dairy.take12.repository;

import com.dairy.take12.model.AdvanceOrganization;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdvanceOrganizationRepo extends MongoRepository<AdvanceOrganization, String> {
}
