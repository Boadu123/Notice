package com.example.notice.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import com.example.notice.Model.NoticeModel;
import com.example.notice.Service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;

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
    
    

}
