package com.example.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.model.Bug;
import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.service.BugService;
import com.example.server.service.ProjectService;
import com.example.server.util.PrincipalUser;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("search")
public class SearchController {
  
  @Autowired
  private ProjectService projectService;
  @Autowired
  private BugService bugService;
  @Autowired
  private PrincipalUser principalUser;

  @GetMapping("title")
  public ResponseEntity<?> searchBugAndProject(@PathParam("title") String title) {
    Map<String, Object> response = new HashMap<>();
    User user = principalUser.getPrincipalUser();
    List<Project> projects = projectService.searchUserProject(user, title);
    List<Bug> bugs = bugService.searchUserBub(user, title);
    response.put("search", "result");
    response.put("projects", projects);
    response.put("bugs", bugs);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
