package com.example.notice.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> getUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<UserModel> users = userService.getAllUsers();

            if (users.isEmpty()) {
                response.put("status", "error");
                response.put("message", "No users Found");
                response.put("details", new ArrayList<>());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("status", "success");
            response.put("message", "All users are available here.");
            response.put("details", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An error occurred while fetching users.");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserModel userModel) {
        Map<String, Object> response = new HashMap<>();
        
        boolean isAuthenticated = userService.authenticate(userModel.getEmail(), userModel.getPassword());
        
        if (isAuthenticated) {
            String token = jwtService.generateToken(userModel.getEmail());
            
            response.put("status", "success");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


}
