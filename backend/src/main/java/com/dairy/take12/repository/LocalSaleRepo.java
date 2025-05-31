package com.dairy.take12.repository;

import com.dairy.take12.model.LocalSale;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocalSaleRepo extends MongoRepository<LocalSale,String> {
}
