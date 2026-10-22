package com.example.employee_system.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${backend.origin}")
    private String ORIGIN;
    @Value("${GMAIL_APP_USERNAME}")
    private String from;

    public void sendAccountCreationEmail(String to, String token) {
        String link = ORIGIN + "/auth/signup/?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Create Your Account!");
        message.setText("Hi! please create your account using this link below:\n" + link);
        javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String link = ORIGIN + "/auth/reset-password/?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Reset your password!");
        message.setText("Hi! click the link below to reset your password:\n" + link);
        javaMailSender.send(message);
    }
}
