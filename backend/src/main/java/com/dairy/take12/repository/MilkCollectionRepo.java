package com.dairy.take12.repository;

import com.dairy.take12.model.MilkCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface MilkCollectionRepo extends MongoRepository<MilkCollection, String> {


}
