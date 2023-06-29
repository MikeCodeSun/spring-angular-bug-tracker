package com.example.server.respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.server.model.Bug;
import com.example.server.model.Project;
import com.example.server.model.User;

public interface BugRepository extends JpaRepository<Bug, Integer> {
  
  @Query("SELECT b FROM Bug b WHERE b.user = ?1")
  List<Bug> findBugsByUser(User user, Sort sort);

  @Query("SELECT b FROM Bug b WHERE b.project = ?1")
  List<Bug> findBugsByProject(Project project, Sort sort);
  
  // page
  @Query("SELECT b FROM Bug b WHERE b.user = ?1")
  Page<Bug> findBugsByUserWithPage(User user,  Pageable pageable);

  //search
  @Query("SELECT b FROM Bug b WHERE (b.user = :user AND b.title LIKE CONCAT('%', :title, '%'))")
  List<Bug> searchUserBug(@Param("user") User user, @Param("title") String title);
}
