package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.response.ServiceResponse;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@RestController
@RequestMapping("/api/v1")
public class SendEmailController {
    
    @Autowired
    CustomerRepository customerRepo;
    
    @Autowired
    CustomerService customerService;
    
    /* new - processing */
    
    @GetMapping("/send-mails")
    public ResponseEntity<ServiceResponse> sendMails() {
        ServiceResponse sendMailResult = customerService.sendMails();
        return new ResponseEntity<>(sendMailResult, HttpStatus.OK);
    }
    
    @GetMapping("/mails")
    public ResponseEntity<ServiceResponse> findAllMail() {
        return new ResponseEntity<>(customerService.findAllEmails(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/send-email")
    @ResponseBody
    public String sendAnEmail() throws MessagingException {
        sendEmail();
        return "Email sent successfully!";
    }
    
    public void sendEmail() throws MessagingException {
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("giangnguyen.developer@gmail.com", "longkhung");
            }
        });
        
        List<String> listEmails = new ArrayList<>();
        List<Customer> customers = customerRepo.findAll();
        
        for (Customer c : customers) {
            listEmails.add(c.getEmail());
        }
        
        Transport transport = session.getTransport();
        transport.connect();
        
        for (String email : listEmails) {
            try {
                Message message = new MimeMessage(session);
                
                // Set sending email address
                message.setFrom(new InternetAddress("giangnguyen.developer@gmail.com", false));
                
                // Set received email address
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                message.setSubject("[Testing Email] This is a test email!");
                
                message.setContent("This is a testing purpose email sending from giangnguyen.developer@gmail.com via smtp google hosting. Sorry for any inconvenience and disturb!", "text/html");
                message.setSentDate(new Date());
                
                MimeBodyPart msgBodyPart = new MimeBodyPart();
                msgBodyPart.setContent("This is a testing purpose email sending from giangnguyen.developer@gmail.com via smtp google hosting. Sorry for any inconvenience and disturb!", "text/html");
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(msgBodyPart);
                
                transport.send(message);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        transport.close();
    }
}
