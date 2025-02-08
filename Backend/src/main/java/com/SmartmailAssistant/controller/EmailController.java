package com.SmartmailAssistant.controller;


import com.SmartmailAssistant.Service.EmailGenaratorService;
import com.SmartmailAssistant.dto.EmailRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin("*")
public class EmailController {

    @Autowired
    private EmailGenaratorService emailGenaratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> getEmail(@RequestBody EmailRequest emailRequest){
        String response = emailGenaratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }

}
