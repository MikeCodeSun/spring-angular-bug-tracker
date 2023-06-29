package com.example.server.model;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "project")
@Getter
@Setter
public class Project {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "title")
  @NotNull(message = "Title must not be null")
  @NotBlank(message = "Title must not be blank")
  @Size(min = 3, max = 50 , message = "Title must be between 3 and 50")
  private String title; 

  @Column(name = "link")
  @NotNull(message = "Link must not be null")
  @NotBlank(message = "Link must not be blank")
  private String link;


  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnoreProperties({"projects", "bugs"})
  @JoinColumn(name = "user_id")
  private User user;

  @CreationTimestamp
  private Date createdAt;

  @UpdateTimestamp
  private Date updateAt;

  @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true )
  @JsonIgnoreProperties({"project", "user"})
  private List<Bug> bugs;

  @PreRemove
  private void removeChildBugs() {
    bugs.removeAll(bugs);
  }
}
