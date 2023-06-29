package com.example.server.respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.model.Project;
import com.example.server.model.User;


public interface ProjectRepostory extends JpaRepository<Project, Integer> {
  
  @Query("SELECT p FROM Project p WHERE p.user = ?1")
  List<Project> findAllProjectsByUser(User user, Sort sort);

  @Query("SELECT p FROM Project p WHERE p.user = ?1")
  Page<Project> findProjectsByUserWithPage(User user, Pageable pageable);

  @Query("SELECT p FROM Project p WHERE (p.user=:user AND p.title LIKE CONCAT('%', :title, '%') )")
  List<Project> SeaerchUserProjects(@Param("user") User user,@Param("title") String title);
}
