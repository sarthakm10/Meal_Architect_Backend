package com.meals.architect.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    // Used for registration
    private String username;
    private String email;
    
    // Used for BOTH registration and login
    private String password;
    
    // For login, we will let React send the user's input (whether it's an email or a username)
    // inside the "identifier" field just to keep things clean.
    private String identifier; 
}