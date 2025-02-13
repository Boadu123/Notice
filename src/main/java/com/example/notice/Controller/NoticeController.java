package com.example.notice.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.example.notice.Model.NoticeModel;
import com.example.notice.Model.UserModel;
import com.example.notice.Repository.UserRepository;
import com.example.notice.Service.JwtService;
import com.example.notice.Service.NoticeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/notices")
    public ResponseEntity<Map<String, Object>> getAllNotices() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get authentication object from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // If user is not authenticated
            if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }

            // Retrieve all products
            List<NoticeModel> products = noticeService.getAllNotices();

            if (products.isEmpty()) {
                response.put("status", "error");
                response.put("message", "No products found.");
                response.put("details", new ArrayList<>());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("status", "success");
            response.put("message", "All products are available.");
            response.put("details", products);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An error occurred while fetching products.");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/notice")
    public ResponseEntity<Map<String, Object>> createNotice(@Validated @RequestBody NoticeModel noticeModel,
            BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // If user is not authenticated
            if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }

            // Get the userId from the token (this assumes you have a method to extract the
            // user ID
            String token = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
            if (token == null) {
                response.put("status", "error");
                response.put("message", "JWT token is missing.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String username = jwtService.extractUsername(token);
            // Check if there are validation errors in the request body
            if (bindingResult.hasErrors()) {
                response.put("status", "error");
                response.put("message", "Invalid input data");
                response.put("details", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            // Retrieve the user from the database based on the userName
            Optional<UserModel> user = userRepository.findByEmail(username);
            if (user == null) {
                response.put("status", "error");
                response.put("message", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Set the user automatically on the productModel
            noticeModel.setUser(user.get());

            // Save the product
            NoticeModel createdProduct = noticeService.createNotice(noticeModel);

            response.put("status", "success");
            response.put("message", "Product created successfully.");
            response.put("details", createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An error occurred while creating the product.");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
