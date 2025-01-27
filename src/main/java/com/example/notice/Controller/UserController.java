package com.example.notice.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.notice.Model.UserModel;
import com.example.notice.Service.JwtService;
import com.example.notice.Service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/users")
    public List<UserModel> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserModel user, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

    
        if (result.hasErrors()) {
            Map<String, String> validationErrors = new HashMap<>();
            result.getFieldErrors().forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
            response.put("status", "error");
            response.put("message", "Validation failed");
            response.put("details", validationErrors);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            userService.registerUser(user);
            String token = jwtService.generateToken(user.getEmail());
            response.put("status", "success");
            response.put("message", "User added successfully");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Error occurred while adding user");
            response.put("details", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


}
