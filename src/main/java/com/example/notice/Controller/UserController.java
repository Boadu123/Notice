package com.example.notice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.notice.Model.UserModel;
import com.example.notice.Service.UserService;

@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public List<UserModel> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public String register(@RequestBody UserModel user){
        try {
            userService.registerUser(user);
            return "User added successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while adding user: " + e.getMessage();
        }
    }

}
