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

import com.example.server.model.Bug;
import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.service.BugService;
import com.example.server.service.CustomUserDetailService;
import com.example.server.service.ProjectService;
import com.example.server.util.PrincipalUser;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("bug")
public class BugController {

  @Autowired
  private BugService bugService;
  @Autowired
  private PrincipalUser principalUser;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private CustomUserDetailService customUserDetailService;

  @PostMapping("add/{project_id}")
  public ResponseEntity<?> addBug(@Valid @RequestBody Bug bug,  BindingResult bindingResult, @PathVariable Integer project_id) {
    Map<String, String> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      for (FieldError fe : bindingResult.getFieldErrors()){
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    Optional<Project> optProjetc = projectService.findProjectById(project_id);
    if(optProjetc.isEmpty()) {
      response.put("Bug", "Project not found");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Project project = optProjetc.get();
    User user = principalUser.getPrincipalUser();
    if(!user.getId().equals(project.getUser().getId())){
      response.put("Bug", "Project not belong to user");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    bug.setUser(user);
    bug.setProject(project);
    Bug newBug = bugService.saveBug(bug);
    return new ResponseEntity<>(newBug, HttpStatus.OK);
  }

  @GetMapping("project/{project_id}")
  public ResponseEntity<?> projectBugs(@PathVariable Integer project_id){
    Map<String, String> response = new HashMap<>();
    Optional<Project> optProject = projectService.findProjectById(project_id);
    if(optProject.isEmpty()) {
      response.put("bug", "project not found");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Project project = optProject.get();
    List<Bug> bugs = bugService.getBugOfProject(project);

    return new ResponseEntity<>(bugs, HttpStatus.OK);
  }

  @GetMapping("mybug")
  public ResponseEntity<?> myBugs() {
    User user = principalUser.getPrincipalUser();
    List<Bug> bugs = bugService.getBugOfUser(user);
    return new ResponseEntity<>(bugs, HttpStatus.OK);
  }

  @GetMapping("user/{user_id}")
  public ResponseEntity<?> userBugs(@PathVariable Integer user_id) {
    Map<String, String> response = new HashMap<>();
    Optional<User> optUser = customUserDetailService.findUserById(user_id);
    if(optUser.isEmpty()) {
      response.put("bug", "User not exist!");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    User user = optUser.get();
    List<Bug> bugs = bugService.getBugOfUser(user);

    return new ResponseEntity<>(bugs, HttpStatus.OK);
  }

  @DeleteMapping("delete/{bug_id}")
  public ResponseEntity<?> deleteBug(@PathVariable Integer bug_id) {
    Map<String, String> response = new HashMap<>();
    Optional<Bug> optBug = bugService.getBugById(bug_id);
    if(optBug.isEmpty()) {
      response.put("bug", "bug not found!");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    User user = principalUser.getPrincipalUser();
    Bug bug = optBug.get();
    if(!user.getId().equals(bug.getUser().getId())) {
      response.put("bug", "bug not belong to user");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    bugService.deleteBug(bug);
    response.put("bug", "delete success");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("id/{bug_id}")
  public ResponseEntity<?> getBug(@PathVariable Integer bug_id) {
    Map<String, String> response = new HashMap<>();
    Optional<Bug> optBug = bugService.getBugById(bug_id);
    if(optBug.isEmpty()) {
      response.put("bug", "bug with id : " + bug_id + "not found!");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Bug bug = optBug.get();
    return new ResponseEntity<>(bug, HttpStatus.OK);
  }

  @PatchMapping("update/{bug_id}")
  public ResponseEntity<?> updateBug(@PathVariable Integer bug_id, @Valid @RequestBody Bug bug, BindingResult bindingResult) {
    Map<String, String> response = new HashMap<>();

    User user = principalUser.getPrincipalUser();
    Optional<Bug> optBug = bugService.getBugById(bug_id);

    if(optBug.isEmpty()){
      response.put("bug", "bug not found");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    Bug oldBug = optBug.get();
    if(!user.getId().equals(oldBug.getUser().getId())){
      response.put("bug", "bug not belong to user, not have authority");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    if(bindingResult.hasErrors()) {
      for(FieldError fe : bindingResult.getFieldErrors()) {
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    oldBug.setDescription(bug.getDescription());
    oldBug.setSolution(bug.getSolution());
    oldBug.setStatus(bug.getStatus());
    oldBug.setTitle(bug.getTitle());
    Bug updateBug = bugService.saveBug(oldBug);

    return new ResponseEntity<>(updateBug, HttpStatus.OK);
  }

  @GetMapping("page/mybug")
  public ResponseEntity<?> getBugsOfPage(@PathParam("page") Integer page,@PathParam("size") Integer size ) {
    
    Map<String, Object> response = new HashMap<>();
    User user = principalUser.getPrincipalUser();
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    Page<Bug> pageBug = bugService.getPageOfUserBug(user, pageable);
    List<Bug> bugs = pageBug.getContent();
    long totalItem = pageBug.getTotalElements();
    Integer totalPage = pageBug.getTotalPages();

    response.put("totalPage", totalPage);
    response.put("totalItem", totalItem);
    response.put("bugs", bugs);
    
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
