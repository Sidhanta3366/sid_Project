package com.example.service;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
//import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
//import javax.mail.internet.MimeMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.InboxEmail;
import com.example.repository.InboxEmailRepository;

@Service
public class MailService {

	
	@Autowired
	private final InboxEmailRepository inboxEmailRepository;
	
	public MailService(InboxEmailRepository inboxEmailRepository) {
		this.inboxEmailRepository = inboxEmailRepository;
	}

	@Autowired
    private JavaMailSender javaMailSender;
    
	
	
    public List<String> readMail() {
        List<String> emails = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.setProperty("mail.store.protocol", "imaps");
            Session emailSession = Session.getDefaultInstance(properties);
            Store emailStore = emailSession.getStore("imaps");
            emailStore.connect("imap.gmail.com", "snayak3366@gmail.com", "foupwplupunnxsow");
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            
            
            Message[] messages = emailFolder.getMessages();
            int i = ((messages.length)-1);
            Message message1 = messages[i];
            System.out.println("from" + (i+1));
            System.out.println("subject "+ message1.getSubject());
            System.out.println("sentDate "+message1.getSentDate());
            System.out.println("contentType "+ message1.getContentType());
            System.out.println("content "+ message1.getContent());
            
            
            for (Message message : messages) {
                String from = InternetAddress.toString(message.getFrom());
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
                String contentType = message.getContentType();
                Object content = message.getContent();
                emails.add(from + " - " + subject + " - " + sentDate + " - " + contentType + " - " + content);
               
                /*List<InboxEmail> email = new ArrayList<>();
                InboxEmail mail = new InboxEmail();
                mail.setContent(content.toString());
                mail.setFrom(from);
                mail.setContentType(contentType);
                mail.setSentDate(sentDate);
                mail.setSubject(subject);
                System.out.println("InboxEmail:" +mail );
                return (List<InboxEmail>) inboxEmailRepository.findAll();
                
                
                	List<InboxEmail> email1 = inboxEmailRepository.findAll();
                	for(InboxEmail inboxemail :email1) {
                	   System.out.println("From: "+inboxemail.getFrom()+"-Subject: "+inboxemail.getSubject()+"-sentDate: "+inboxemail.getSentDate()+"-contentType: "
                			   +inboxemail.getContentType()+"content: "+inboxemail.getContent());	
                	}*/
                
                	
                	/*// create session
                	Session session = Session.getInstance(properties);

                	// connect to store
                	Store store = session.getStore("imaps");
                	store.connect("imap.gmail.com","snayak3366@gmail.com", "gblhbgzynlmilxew");

                	// get inbox folder
                	Folder inbox = store.getFolder("inbox");
                	inbox.open(Folder.READ_WRITE);
                	
                	// get all messages
                	Message[] messages1 = inbox.getMessages();

                	// loop through messages
                	for (@SuppressWarnings("unused") Message message2 : messages1) {
                	    // read message content
                	    String content1 = message.getContent().toString();
                	    
                	    // mark message as read
                	    message.setFlag(Flags.Flag.SEEN, true);

                	    // create new message
                	    MimeMessage newMessage = new MimeMessage((MimeMessage) message);
                	    newMessage.setContent(content1, message.getContentType());

                	    // mark original message as deleted
                	 //  message.setFlag(Flags.Flag.DELETED, true);

                	    // get drafts folder
                	    Folder drafts = store.getFolder("Drafts");
                	    if (!drafts.exists()) {
                	        drafts.create(Folder.HOLDS_MESSAGES);
                	    }
                	    drafts.open(Folder.READ_WRITE);

                	    // append new message to drafts folder
                	    Message[] draftMessages = {newMessage};
                	    drafts.appendMessages(draftMessages);

                	    // close drafts folder
                	    drafts.close(false);
                	}

                	// close inbox folder
                	inbox.close(false);

                	// close store
                	store.close();*/
            }
              emailFolder.close(false);
            emailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }
    

    //Save inbox email into database
    public void saveInboxEmails() {
    	System.out.println("Saving inbox emails at "+ new Date());
        List<InboxEmail> inboxEmails = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.setProperty("mail.store.protocol", "imaps");
            Session emailSession = Session.getDefaultInstance(properties);
            Store emailStore = emailSession.getStore("imaps");
            emailStore.connect("imap.gmail.com", "snayak3366@gmail.com", "foupwplupunnxsow");
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            for (Message message : messages) {
                String from = InternetAddress.toString(message.getFrom());
                String subject = message.getSubject();
                Date sentDate = message.getSentDate();
                String contentType = message.getContentType();
                Object content = message.getContent();
                InboxEmail inboxEmail = new InboxEmail(from, subject, sentDate, contentType, content.toString());
                inboxEmails.add(inboxEmail);
            }

            emailFolder.close(false);
            emailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        inboxEmailRepository.saveAll(inboxEmails);
    }
    
    
    
    //DElete inbox mail by giving the Subject
    @Transactional
    public void deleteInboxMailBySubject(String subject) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.ssl.trust", "*");
        
        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", "snayak3366@gmail.com", "foupwplupunnxsow");
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_WRITE);
        SearchTerm searchTerm = new SubjectTerm(subject);
        Message[] messages = inbox.search(searchTerm);
        for (Message message : messages) {
            message.setFlag(Flags.Flag.DELETED, true);
        }
        inbox.close(true);
        store.close();
    }
 
    //Inbox mail move to draft folder after giving the Subject
    @Transactional
    public void moveInboxMailToDrafts(String subject) throws MessagingException, IOException {
    	Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.ssl.trust", "*");
        
        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", "snayak3366@gmail.com", "foupwplupunnxsow");
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_WRITE);
        Folder drafts = store.getFolder("[Gmail]/Drafts");
        drafts.open(Folder.READ_WRITE);
        SearchTerm searchTerm = new SubjectTerm(subject);
        Message[] messages = inbox.search(searchTerm);
        for (Message message : messages) {
            Message draftsMessage = new MimeMessage(session);
            draftsMessage.setSubject(message.getSubject());
            draftsMessage.setFrom(message.getFrom()[0]);
            draftsMessage.setRecipient(Message.RecipientType.TO, message.getRecipients(Message.RecipientType.TO)[0]);
            try {
            Object content = message.getContent();
            String contentString = "";
            if (content instanceof String) {
                contentString = (String) content;
            } else if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                int count = multipart.getCount();
                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    try {
                    Object bodyPartContent = bodyPart.getContent();
                    if (bodyPartContent instanceof String) {
                        contentString += (String) bodyPartContent;
                    }
                }catch (MessagingException e) {
                    e.printStackTrace();
            }
         }
       }
            draftsMessage.setContent(contentString, message.getContentType());
            }catch (MessagingException e) {
                e.printStackTrace();
            }
            drafts.appendMessages(new Message[] { draftsMessage });
            message.setFlag(Flags.Flag.DELETED, true);
        }
        inbox.close(true);
        drafts.close(true);
        store.close();
    }
  }

