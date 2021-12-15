package com.example.demo.repos;

import com.example.demo.entiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepo extends JpaRepository<User,Long> {
    UserDetails findByUsername(String username);
    Optional<User> findById(Long id);
}
