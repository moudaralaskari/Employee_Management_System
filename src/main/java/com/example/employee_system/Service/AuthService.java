package com.example.employee_system.Service;

import com.example.employee_system.Config.JwtHelper;
import com.example.employee_system.Dto.ResetPasswordRequest;
import com.example.employee_system.Dto.SignupRequest;
import com.example.employee_system.Dto.loginRequest;
import com.example.employee_system.Entity.Employee;
import com.example.employee_system.Entity.PasswordResetToken;
import com.example.employee_system.Entity.UserAccount;
import com.example.employee_system.Repository.EmployeeRepo;
import com.example.employee_system.Repository.PasswordResetRepo;
import com.example.employee_system.Repository.UserAccountRepo;
import com.example.employee_system.Shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private PasswordResetRepo passwordResetRepo;
    @Autowired
    private EmailService emailService;

    public void signup(SignupRequest signupRequest, String token) {

        Employee employee = employeeRepo.findOneByAccountCreationToken(token).
                orElseThrow(() -> CustomResponseException.ResourceNotFound("Invalid token"));
        if (employee.isVerified()) {
            throw CustomResponseException.BadRequest("Account already created");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(signupRequest.username());
        userAccount.setPassword(passwordEncoder.encode(signupRequest.password()));
        userAccount.setEmployee(employee);
        userAccountRepo.save(userAccount);

        employee.setVerified(true);
        employee.setAccountCreationToken(null);
        employeeRepo.save(employee);
    }


    public String login(loginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        UserAccount user = userAccountRepo.findOneByUsername(loginRequest.username())
                .orElseThrow(CustomResponseException::BadCredentials);
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("userId", user.getId());
        return jwtHelper.generateToken(customClaims, user);
    }

    @Transactional
    public void initiatePasswordReset(String username) {
        try {
            UserAccount user = userAccountRepo.findOneByUsername(username)
                    .orElseThrow(() -> CustomResponseException.ResourceNotFound("Account not found"));
            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
            PasswordResetToken resetToken = new PasswordResetToken();

            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(expiry);

            passwordResetRepo.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmployee().getEmail(), token);
        } catch (Exception e) {
            throw CustomResponseException.BadRequest("Sending reset password email failed");
        }

    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetRepo.findOneByToken(resetPasswordRequest.token())
                .orElseThrow(() -> CustomResponseException.BadRequest("Invalid token"));
        boolean isTokenExpired = passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now());
        if (isTokenExpired) {
            passwordResetRepo.delete(passwordResetToken);
            throw CustomResponseException.BadRequest("Token has expired, request a new one");
        }
        UserAccount user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.password()));
        userAccountRepo.save(user);
        passwordResetRepo.delete(passwordResetToken);
    }
}
