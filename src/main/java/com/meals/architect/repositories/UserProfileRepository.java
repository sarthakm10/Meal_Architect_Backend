package com.meals.architect.repositories;

import com.meals.architect.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Spring Data JPA gives us findById() and save() automatically!
}