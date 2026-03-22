package com.meals.architect.repositories;

import com.meals.architect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring automatically writes the SQL: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Useful for checking if an OAuth user exists via their Google ID
    Optional<User> findByProviderId(String providerId);
}