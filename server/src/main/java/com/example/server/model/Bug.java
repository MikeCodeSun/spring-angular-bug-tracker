package com.example.server.model;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.server.util.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bug")
@Getter
@Setter
public class Bug {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "title")
  @NotBlank(message = "Bug title must not be blank")
  @NotEmpty(message = "Bug title must not be empty")
  @NotNull(message = "Bug title must not be bull")
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "solution", columnDefinition = "TEXT")
  private String solution;

  @Enumerated
  @Column(name = "status")
  private Status status = Status.UNSOLVED;

  @CreationTimestamp
  private Date createdAt;

  @UpdateTimestamp
  private Date updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id")
  @JsonIgnoreProperties({"bugs","user"})
  private Project project;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @JsonIgnoreProperties({"bugs","projects"})
  private User user;

  @PreRemove
  private void removeBugFromUserAndProject(){
    project.getBugs().remove(this);
    user.getBugs().remove(this);
  }

}

