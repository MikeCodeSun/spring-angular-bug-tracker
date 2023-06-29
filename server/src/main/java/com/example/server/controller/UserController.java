package com.example.server.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.LoginRequseBody;
import com.example.server.model.UpdateNameRequestBody;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;
import com.example.server.util.EncryptPassword;
import com.example.server.util.PrincipalUser;
import com.example.server.util.TokenUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {
  
  @Autowired 
  private CustomUserDetailService customUserDetailService;
  @Autowired 
  private EncryptPassword encryptPassword;
  @Autowired
  private AuthenticationManager authManager;
  @Autowired
  private TokenUtil tokenUtil;
  @Autowired 
  private PrincipalUser principalUser;


  @PostMapping("register")
  public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult) {
    Map<String, String> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      
      
      for(FieldError fe:bindingResult.getFieldErrors()) {
        response.put(fe.getField(), fe.getDefaultMessage());
        
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    String encodePassword = encryptPassword.hashPassword(user.getPassword());
    user.setPassword(encodePassword);
    customUserDetailService.saveUserToDb(user);
    response.put("login", "success");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }  

  
  @PostMapping("login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequseBody loginRequseBody, BindingResult bindingResult) {
    Map<String, String> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      for(FieldError fe:bindingResult.getFieldErrors()) {
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    try {
      Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequseBody.getEmail(), loginRequseBody.getPassword()));
      CustomUserDetail customUserDetail =(CustomUserDetail)auth.getPrincipal();
      String token = tokenUtil.generateJwtToken(customUserDetail);
      response.put("login", "success");
      response.put("token", token);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      response.put("password", "Password not right");
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("hello")
  public String hello() {
    User user = principalUser.getPrincipalUser();
    return "hello " + user.getName() + "!";
  }

  @GetMapping("profile/{id}")
  public ResponseEntity<?> getProfile(@PathVariable("id") Integer id) {
    Map<String, String> response = new HashMap<>();
    Optional<User> optUser = customUserDetailService.findUserById(id);
    if(optUser.isEmpty()) {
      response.put("user", "user id:" + id + "not exist");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    User user = optUser.get();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  
  @PatchMapping("name")
  public ResponseEntity<?> changeName(@RequestBody @Valid UpdateNameRequestBody updateNameRequest, BindingResult bindingResult) {
    Map<String, String> response = new HashMap<>();

    if(bindingResult.hasErrors()) {
      for(FieldError fe : bindingResult.getFieldErrors()){
        response.put(fe.getField(), fe.getDefaultMessage());
      }
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    Optional<User> optUser = customUserDetailService.findUserById(updateNameRequest.getId());

    if(optUser.isEmpty()) {
      response.put("user","User not exist!");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    User updateUser = optUser.get();

    User currentUser = principalUser.getPrincipalUser();

    if(!currentUser.getId().equals(  updateUser.getId())){
      response.put("user","not have authorirty to change this user name");
      return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    
    updateUser.setName(updateNameRequest.getName());
    customUserDetailService.saveUserToDb(updateUser);

    response.put("name", updateUser.getName());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("image/{id}")
  public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile,@PathVariable Integer id){
    Map<String, String> response = new HashMap<>();
    
    // get file info
    String originalName = multipartFile.getOriginalFilename();
    String type = multipartFile.getContentType();
    long size = multipartFile.getSize();
    int index = originalName.lastIndexOf(".");
    
    // check is current is change hiself image
    User currentUser = principalUser.getPrincipalUser();
    if(!currentUser.getId().equals(id)){
      response.put("image", "user not allow to change other's image");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // check is user exist
    Optional<User> optUser = customUserDetailService.findUserById(id);
    if(optUser.isEmpty()) {
      response.put("image", "user exist");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    User user = optUser.get();
    
    // create new image name with raddom uuid + extension name
    UUID uuid = UUID.randomUUID();
    String ext = "";
    if(index > 0 ) {
        ext = originalName.substring(index+1);
    }
    String imageName = uuid + "." + ext;

    // check image is empty
    if(multipartFile.isEmpty()) {
      response.put("image", "image must not be empty");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // check image size
    if(size > 1024000) {
      response.put("image", "image size must less than 1mb");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // check image type
    if(!type.equals("image/jpeg") && !type.equals("image/png") && !type.equals("image/jpg") ){
      response.put("image", "image file must be jpg,png or jpeg");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // check if image folder exist ,if not ,create one
    Path path = Paths.get("image");
    if(Files.notExists(path)){
      try {
        Files.createDirectories(path);
      } catch (Exception e) {
        response.put("image", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    // check user have image .if have delete old one
    if(user.getImage() != null) {
      Path oldImagePath = Paths.get("image", user.getImage());
      try {
        Files.deleteIfExists(oldImagePath);
      } catch (Exception e) {
        response.put("image", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    // save image to file system
    Path imagePath = Paths.get("image", imageName);
    try {
      Files.copy(multipartFile.getInputStream(),imagePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      response.put("image", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // save new image to db 
    user.setImage(imageName);
    customUserDetailService.saveUserToDb(user);
    
    response.put("image", imageName);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
