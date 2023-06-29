package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.server.model.Bug;
import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.respository.BugRepository;

@Service
public class BugService {
  @Autowired
  private BugRepository bugRepository;

  public Bug saveBug(Bug bug) {
    return bugRepository.save(bug);
  }
  public Optional<Bug> getBugById(Integer id){
    return bugRepository.findById(id);
  }
  public void deleteBug(Bug bug) {
    bugRepository.delete(bug);
  }
  public List<Bug> getBugOfUser(User user) {
    return bugRepository.findBugsByUser(user, Sort.by(Sort.Direction.DESC, "id"));
  }

  public List<Bug> getBugOfProject(Project project) {
    return bugRepository.findBugsByProject(project, Sort.by(Sort.Direction.DESC, "id"));
  }

  public Page<Bug> getPageOfUserBug(User user, Pageable pageable) {
    return bugRepository.findBugsByUserWithPage(user, pageable);
  }

  public List<Bug> searchUserBub(User user, String title) {
    return bugRepository.searchUserBug(user, title);
  }
  
}
