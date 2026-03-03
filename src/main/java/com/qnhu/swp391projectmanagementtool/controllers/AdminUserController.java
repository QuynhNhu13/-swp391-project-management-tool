package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping("/export-csv")
    public ResponseEntity<byte[]> exportUsersToCsv() {

        List<User> users = userRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("emailAddress,displayName\n");

        for (User user : users) {
            csv.append(user.getEmail())
                    .append(",")
                    .append(user.getUsername())
                    .append("\n");
        }

        byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @GetMapping("/pending-jira")
    public List<User> getPendingJiraUsers() {
        return userRepository.findByJiraSyncedFalse();
    }
}