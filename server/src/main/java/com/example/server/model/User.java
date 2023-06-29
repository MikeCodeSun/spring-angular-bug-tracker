package com.example.server.model;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.server.validator.Unique;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "email")
  @NotBlank(message = "Email must not be blank")
  @NotEmpty(message = "Email must not be empty")
  @NotNull(message = "Email must not be null")
  @Email(message = "Email address not valid")
  @Unique(message = "Email has been token!")
  private String email;

  @Column(name = "name")
  @NotBlank(message = "Name must not be blank")
  @NotEmpty(message = "Name must not be empty")
  @NotNull(message = "Name must not be null")
  @Size(max = 20, min = 3, message = "Name must between 3 and 20")
  private String name;

  @Column(name = "password")
  @NotBlank(message = "Password must not be blank")
  @NotEmpty(message = "Password must not be empty")
  @NotNull(message = "Password must not be null")
  @Size(min = 6, message = "Password must more than 6")
  private String password;

  @Column(name = "image")
  private String image;

  @CreationTimestamp
  private Date createdAt;

  @UpdateTimestamp
  private Date updatedAt;

  
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
  @JsonIgnoreProperties({"user", "bugs"})
  private List<Project> projects;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnoreProperties({"user", "project"})
  private List<Bug> bugs;

}
