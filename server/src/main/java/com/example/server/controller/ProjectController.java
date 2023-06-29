package com.example.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;
import com.example.server.service.ProjectService;
import com.example.server.util.PrincipalUser;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("project")
public class ProjectController {
  
  @Autowired
  private ProjectService projectService;
  @Autowired
  private PrincipalUser principalUser;
  @Autowired
  private CustomUserDetailService customUserDetailService;

  @PostMapping("add")
  public ResponseEntity<?> addProject(@Valid @RequestBody Project project, BindingResult bindingResult) {

    Map<String, String> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      for(FieldError fe : bindingResult.getFieldErrors()){
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    User user = principalUser.getPrincipalUser();
    project.setUser(user);

    Project newProject = projectService.saveProject(project);
    return new ResponseEntity<>(newProject, HttpStatus.OK);
  }

  @DeleteMapping("delete/{project_id}")
  public ResponseEntity<?> deleteOneProject(@PathVariable Integer project_id) {
    Map<String, String> response = new HashMap<>();
    Optional<Project> optProject = projectService.findProjectById(project_id);

    if(optProject.isEmpty()) {
      response.put("project", "Project with Id: " + project_id + " Not found!");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Project project = optProject.get();
    User currentUser = principalUser.getPrincipalUser();
    if(!currentUser.getId().equals(project.getUser().getId())){
      response.put("project", "Project with Id: " + project_id + " Not Belong to user, And Not have authority to delete" );
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    currentUser.getProjects().removeIf(p -> p.getId() == project.getId());
    customUserDetailService.saveUserToDb(currentUser);
    projectService.deleteProject(project);
    response.put("project", "delete success" );
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("all")
  public ResponseEntity<?> allProjects() {
    
    List<Project> projects = projectService.findAllProjects();
    return new ResponseEntity<>(projects, HttpStatus.OK);
  }

  @GetMapping("id/{project_id}")
  public ResponseEntity<?> OneProject(@PathVariable Integer project_id) {
    Map<String, String> response = new HashMap<>();
    Optional<Project> optProject = projectService.findProjectById(project_id);
    if(optProject.isEmpty()){
      response.put("project", "project id: " + project_id + " Not found");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Project project = optProject.get();
    return new ResponseEntity<>(project, HttpStatus.OK);
  }

  @PatchMapping("update/{project_id}")
  public ResponseEntity<?> updateProject(@PathVariable Integer project_id, @Valid @RequestBody Project project, BindingResult bindingResult) {
    Map<String, String> response = new HashMap<>();

    Optional<Project> optProject = projectService.findProjectById(project_id);
    if(optProject.isEmpty()) {
      response.put("project", "Project with Id: " + project_id + " Not found!");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    Project oldProject = optProject.get();
    User currentUser = principalUser.getPrincipalUser();
    if(!currentUser.getId().equals(oldProject.getUser().getId())){
      response.put("project", "Project with Id: " + project_id + " Not Belong to user, And Not have authority to update" );
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    if(bindingResult.hasErrors()) {
      for(FieldError fe : bindingResult.getFieldErrors()){
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    oldProject.setTitle(project.getTitle());
    oldProject.setDescription(project.getDescription());
    oldProject.setLink(project.getLink());

    Project updateProject = projectService.saveProject(oldProject);

    return new ResponseEntity<>(updateProject, HttpStatus.OK);
  }

  @GetMapping("user/{user_id}")
  public ResponseEntity<?> getUserProjects(@PathVariable Integer user_id) {
    Map<String, String> response = new HashMap<>();
    Optional<User> optUser = customUserDetailService.findUserById(user_id);
    if(optUser.isEmpty()) {
      response.put("project", "User id : " + user_id + " Not Found.");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    User user = optUser.get();
    List<Project> projects = projectService.findProjectsOfUser(user);

    return new ResponseEntity<>(projects, HttpStatus.OK);
  }

  @GetMapping("myproject")
  public ResponseEntity<?> getMyProjects(){
    User user = principalUser.getPrincipalUser();
    List<Project> projects = projectService.findProjectsOfUser(user);
    return new ResponseEntity<>(projects, HttpStatus.OK);
  }

  @GetMapping("page/myproject")
  public ResponseEntity<?> getMyProjectsWithPage(@PathParam("page") Integer page, @PathParam("size") Integer size) {
    Map<String, Object> response = new HashMap<>();

    User user = principalUser.getPrincipalUser();
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    Page<Project> pageProject = projectService.getProjectsOfUserWithPage(user, pageable);

    long totalItem = pageProject.getTotalElements();
    Integer totalPage = pageProject.getTotalPages();
    List<Project> projects = pageProject.getContent();

    response.put("totalItem", totalItem);
    response.put("totalPage", totalPage);
    response.put("projects", projects);

    return new ResponseEntity<>(response, HttpStatus.OK);

  }
}
