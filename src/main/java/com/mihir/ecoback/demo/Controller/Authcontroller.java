package com.mihir.ecoback.demo.Controller;

import com.mihir.ecoback.demo.JwtToken.JwtUtil;
import com.mihir.ecoback.demo.Models.UserModel;
import com.mihir.ecoback.demo.Repo.UserRepo;
import com.mihir.ecoback.demo.Services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class Authcontroller {
    @Autowired
    private UserRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

//    @PostMapping("user/register")
//    public String register(@RequestBody UserModel userModel) {
//        if (repo.findByEmail(userModel.getEmail()).isPresent()) {
//            return "user already exist";
//        }
//        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
//        userModel.setRole("ROLE_USER");
//        repo.save(userModel);
//        return "user registed successfully";
//    }
//    @PostMapping("/admin/register")
//    public String registerAdmin(@RequestBody UserModel userModel) {
//        if (repo.findByEmail(userModel.getEmail()).isPresent()) {
//            return "admin already exist";
//        }
//        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
//        userModel.setRole("ROLE_ADMIN");
//        repo.save(userModel);
//        return "Admin registered successfully";
//    }
    @PostMapping("/register")
    public String register(@RequestBody UserModel userModel){
        if (repo.findByEmail(userModel.getEmail()).isPresent()) {
            return "admin already exist";
        }
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setRole(userModel.getRole());
        repo.save(userModel);
        return "Admin registered successfully";

    }

//    @PostMapping("/admin/login")
//    public ResponseEntity<?> loginadmin(@RequestBody UserModel userModel) {
//        String result= customUserDetailsService.loginasAdmin(
//                userModel.getEmail(),
//                userModel.getPassword(),
//                passwordEncoder
//        );
//        if (result.startsWith("admin login")) {
//            // Generate token
//            String token = jwtUtil.generateToken(userModel.getEmail(), "ADMIN");
//            // Get user from DB
//            Optional<UserModel> admin = repo.findByEmail(userModel.getEmail());
//            if (admin.isPresent()) {
//                // Return both token and user data
//                return ResponseEntity.ok().body(Map.of(
//                        "token", "Bearer " + token,
//                        "admin", admin.get()
//                ));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("admin not found");
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
//        }
//    }
//    @PostMapping("/user/login")
//    public ResponseEntity<?> loginuser(@RequestBody UserModel userModel) {
//        String result = customUserDetailsService.loginasUser(
//                userModel.getEmail(),
//                userModel.getPassword(),
//                passwordEncoder
//        );
//
//        if (result.startsWith("user login")) {
//            // Generate token
//            String token = jwtUtil.generateToken(userModel.getEmail(), "USER");
//
//            // Get user from DB
//            Optional<UserModel> user = repo.findByEmail(userModel.getEmail());
//
//            if (user.isPresent()) {
//                // Return both token and user data
//                return ResponseEntity.ok().body(Map.of(
//                        "token", "Bearer " + token,
//                        "user", user.get()
//                ));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        String result = customUserDetailsService.login(
                userModel.getEmail(),
                userModel.getPassword(),
                passwordEncoder
        );

        if (!result.startsWith("login success")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        // Extract role
        String role = result.split(":")[1]; // ROLE_USER or ROLE_ADMIN

        String token = jwtUtil.generateToken(userModel.getEmail(), role);

        Optional<UserModel> user = repo.findByEmail(userModel.getEmail());

        if (user.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "token", "Bearer " + token,
                    "user", user.get(),
                    "role", role
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("skosdosdko");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        String email = authentication.getName();
        System.out.println("Email from token: " + email);// Get email from authentication
        Optional<UserModel> user = repo.findByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

//    @PostMapping("/user/logout")
//    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
//        // Optionally do some logic here
//        // You can get the token if needed:
//        String authHeader = request.getHeader("Authorization");
//
//        // Just send response to frontend that token should be deleted
//        return ResponseEntity.ok(Map.of(
//                "message", "Logout successful. Remove token on frontend."
//        ));
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        // Optionally do some logic here
        // You can get the token if needed:
        String authHeader = request.getHeader("Authorization");

        // Just send response to frontend that token should be deleted
        return ResponseEntity.ok(Map.of(
                "message", "Logout successful. Remove token on frontend."
        ));
    }
    @PutMapping("user/update")
    public ResponseEntity<String> updateUser(@RequestBody UserModel updatedUser, Authentication authentication) {
        System.out.println("==== DEBUG LOG ====");
        System.out.println("Authentication: " + authentication);
        System.out.println("Email from auth.getName(): " + authentication.getName());
        System.out.println("===================");
        String email = authentication.getName();  // gets email from token
        Optional<UserModel> existingUserOpt = repo.findByUsername(email);

        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UserModel existingUser = existingUserOpt.get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());

        repo.save(existingUser);

        return ResponseEntity.ok("User updated successfully");
    }

}
