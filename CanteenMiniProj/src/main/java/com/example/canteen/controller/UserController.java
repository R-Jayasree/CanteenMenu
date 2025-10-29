package com.example.canteen.controller;

import com.example.canteen.model.User;
import com.example.canteen.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/public")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/user/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // New authenticate endpoint
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        Optional<User> optionalUser = userService.getUserByEmail(authRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // For demonstration, assuming plain text password matching
            if (user.getPassword().equals(authRequest.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", "fake-jwt-token"); // Replace with real token logic later
                response.put("userId", user.getId());
                response.put("role", user.getRole());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    // DTO class for authentication request
    public static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
