package com.dairy.take12.repository;

import com.dairy.take12.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface RefreshTokenRepo extends MongoRepository<RefreshToken ,String> {
    Optional<RefreshToken> findByToken(String token);
    boolean existsByToken(String token);
    // Delete RefreshToken by token string
    void deleteByToken(String token);
}
