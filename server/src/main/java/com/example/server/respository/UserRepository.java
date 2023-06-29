package com.example.server.respository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.model.User;


public interface UserRepository extends JpaRepository<User, Integer>{
  
  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findUserByEmail(String email);
}
