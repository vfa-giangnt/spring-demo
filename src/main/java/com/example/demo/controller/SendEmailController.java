package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
public class SendEmailController {
    
    @Autowired
    CustomerRepository customerRepo;
    
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
        
        List<Customer> customers = customerRepo.findAll();
        
        String[] listEmails = {"vfa.trongvn@gmail.com", "vfa.giangnt@gmail.com", "vfa.uyentb@gmail.com", "giangnguyen.developer@gmail.com"};
        
        Transport transport = session.getTransport();
        transport.connect();
        
        for (String email : listEmails) {
            
            // Create session to send message
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
            
            // Add attach files
//        MimeBodyPart attachPart = new MimeBodyPart();
//        multipart.addBodyPart(attachPart);
            
            transport.send(message);
        }
        transport.close();
    }
}
