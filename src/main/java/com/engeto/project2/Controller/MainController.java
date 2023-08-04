package com.engeto.project2.Controller;

import com.engeto.project2.Service.UserRepository;
import com.engeto.project2.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private UserRepository userRepository;

    @Autowired
    public MainController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping ("/api/v1/user/{ID}")
    public String findUser(@PathVariable int ID){
        return "JSON";
    }

    @PostMapping ("api/v1/user")
    public String userCreation(@RequestBody String name, String surname, String personID){
        return "User saved!" + name + surname + personID ;
    }

    @GetMapping ("/api/v1/user")
    public List<User> getAll(){
        return userRepository.selectAll();
    }


}
