package com.meals.architect.controller;

import com.meals.architect.dto.AuthRequestDTO;
import com.meals.architect.models.User;
import com.meals.architect.models.UserProfile;
import com.meals.architect.repositories.UserProfileRepository;
import com.meals.architect.repositories.UserRepository;
import com.meals.architect.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    // ==========================================
    // 1. LOCAL REGISTRATION (Username, Email & Password)
    // ==========================================
    @PostMapping("/register")
    public ResponseEntity<?> registerLocalUser(@RequestBody AuthRequestDTO request) {
        // Check if username OR email is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Scramble it!
        newUser.setAuthProvider("local");

        User savedUser = userRepository.save(newUser);
        
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profileRepository.save(profile);

        String token = jwtService.generateToken(savedUser.getUsername());
        return ResponseEntity.ok(Map.of("token", token, "userId", savedUser.getId()));
    }

    // ==========================================
    // 2. LOCAL LOGIN (Username OR Email)
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<?> loginLocalUser(@RequestBody AuthRequestDTO request) {
        // The magic method! Finds them whether they typed 'developer' or 'developer@example.com'
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(
                request.getIdentifier(), 
                request.getIdentifier()
        );

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user.getUsername());
                return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // ==========================================
    // 3. GOOGLE LOGIN SYNC
    // ==========================================
    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, String> payload) {
        String googleId = payload.get("googleId");
        String email = payload.get("email");

        Optional<User> existingUser = userRepository.findByAuthProviderId(googleId);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // New Google User! Let's auto-generate a username.
            // Grabs "developer" out of "developer@example.com"
            String baseUsername = email.split("@")[0]; 
            String finalUsername = baseUsername;

            // If that username is already taken by a local user, append a short random string
            if (userRepository.findByUsername(baseUsername).isPresent()) {
                finalUsername = baseUsername + "_" + UUID.randomUUID().toString().substring(0, 5);
            }

            User newUser = new User();
            newUser.setUsername(finalUsername); 
            newUser.setEmail(email);
            newUser.setAuthProvider("google");
            newUser.setAuthProviderId(googleId);
            
            user = userRepository.save(newUser);

            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profileRepository.save(profile);
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
    }
}