package com.example.employee_system.Controller;

import com.example.employee_system.Dto.ResetPasswordRequest;
import com.example.employee_system.Dto.SignupRequest;
import com.example.employee_system.Dto.loginRequest;
import com.example.employee_system.Service.AuthService;
import com.example.employee_system.Shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(@RequestBody loginRequest loginRequest) {

        String token = authService.login(loginRequest);
        return new ResponseEntity<>(new GlobalResponse<>(token), HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<String>> signup(@RequestBody SignupRequest signupRequest, @RequestParam String token) {
//        System.out.println("token " + token);
        authService.signup(signupRequest, token);
        return new ResponseEntity<>(new GlobalResponse<>("sign up"), HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password/{username}")
    public ResponseEntity<GlobalResponse<String>> forgotPassword(@PathVariable String username) {
//        System.out.println("token " + token);
        authService.initiatePasswordReset(username);
        return new ResponseEntity<>(new GlobalResponse<>("Password reset email sent!"), HttpStatus.CREATED);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GlobalResponse<String>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
//        System.out.println("token " + token);
        authService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>(new GlobalResponse<>("Password reset successfully!"), HttpStatus.CREATED);
    }
}
