package com.example.social.repository;

import com.example.social.entities.Post;
import com.example.social.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContaining(String name, String surname);
}
