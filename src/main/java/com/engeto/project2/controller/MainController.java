package com.engeto.project2.controller;

import com.engeto.project2.ErrorMessages;
import com.engeto.project2.repository.UserRepository;
import com.engeto.project2.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.engeto.project2.UserInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping ("/{id}")
    public ResponseEntity<String> findUser(@PathVariable ("id") int userId,
                                           @RequestParam (value = "detail", defaultValue = "false") boolean detail){
        UserInfo user = userService.getUserInfoById(userId,detail);
        if (user != null) {
            try {
                return ResponseEntity.ok(UserService.convertToJson(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<String> findAllUsers(@RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        List<UserInfo> users = userService.getAllUsers(detail);
        if (!users.isEmpty()) {
            try {
                return ResponseEntity.ok(UserService.convertToJson(users));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.USER_NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserInfo newUser) {
        String personID = newUser.getPersonId();

        if (userService.checkUseOfPersonID(personID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.USER_EXIST);
        }

        UserInfo createdUser = userService.postCreateUser(newUser);
        if (createdUser != null) {
            try {
                return ResponseEntity.ok(UserService.convertToJson(createdUser));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
        }
    }

    @PutMapping ()
    public ResponseEntity<String> updateUser(@RequestBody UserInfo updatedUser){
        boolean updated = userService.putUpdateUser(updatedUser);
        if (updated) {
            try {
                return ResponseEntity.ok(UserService.convertToJson(updatedUser));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.ERROR_OCCURRED);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable ("id") int userId){
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(ErrorMessages.USER_DELETED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.USER_NOT_FOUND);
        }
    }
}
