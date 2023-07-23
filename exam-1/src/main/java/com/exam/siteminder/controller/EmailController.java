package com.exam.siteminder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.siteminder.pojo.EmailRequest;
import com.exam.siteminder.service.EmailService;

/**
 * This is a code to send email using API.
 */
@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<Object> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            return emailService.sendEmailWithFailover(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}
