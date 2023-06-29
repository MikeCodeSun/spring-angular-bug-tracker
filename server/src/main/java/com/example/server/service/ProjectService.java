package com.example.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.respository.ProjectRepostory;

@Service
public class ProjectService {
  @Autowired
  private ProjectRepostory projectRepostory;

  public Project saveProject(Project project) {
    return projectRepostory.save(project);
  }

  public Optional<Project> findProjectById(Integer id) {
    return projectRepostory.findById(id);
  }

  public void deleteProject(Project project) {
    projectRepostory.delete(project);
  }

  public List<Project> findAllProjects() {
    return projectRepostory.findAll();
  }

  public List<Project> findProjectsOfUser(User user){
    return projectRepostory.findAllProjectsByUser(user, Sort.by(Sort.Direction.DESC, "id"));
  }

  public Page<Project> getProjectsOfUserWithPage(User user, Pageable pageable) {
    return projectRepostory.findProjectsByUserWithPage(user, pageable);
  }

  public List<Project> searchUserProject(User user, String title) {
    return projectRepostory.SeaerchUserProjects(user, title);
  }
}
