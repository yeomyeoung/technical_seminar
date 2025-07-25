package com.example.ZeroTrust.repository;

import com.example.ZeroTrust.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserID(String userID);
}
