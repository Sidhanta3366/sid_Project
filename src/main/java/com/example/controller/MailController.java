package com.example.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.InboxEmail;
import com.example.service.MailService;


@RequestMapping("/mail")
@RestController
public class MailController {
	

	
	@Autowired
	private MailService mailService;
	
	
	//Read all mail in postman
	@PostMapping("/inbox-emails")
	private List<String> saveData(InboxEmail inboxEmail){
		return mailService.readMail();
	}
	
	//Save inbox mail into database
	//http://localhost:7070/mail/save
	@PostMapping("/save")
    public ResponseEntity<String> saveInboxEmails() {
        try {
            mailService.saveInboxEmails();
            return ResponseEntity.ok("Inbox emails saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save inbox emails.");
        }
    }
	
	@GetMapping("/inbox")
	public List<String> getInbox(){
		return mailService.readMail();
	}

	//Delete inbox mail by subject
	//http://localhost:7070/mail/{Subject}
	@DeleteMapping("/{subject}")
    public void deleteMailBySubject(@PathVariable String subject) throws MessagingException {
        mailService.deleteInboxMailBySubject(subject);
    }
	
	//move inbox mail into drafts folder
	@PostMapping("/{subject}/move-to-drafts")
    public void moveMailToDrafts(@PathVariable String subject) throws MessagingException, IOException {
        mailService.moveInboxMailToDrafts(subject);
    }
}
