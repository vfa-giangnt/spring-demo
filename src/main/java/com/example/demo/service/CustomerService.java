package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.response.ServiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class CustomerService {
    
    @Autowired
    CustomerRepository customerRepo;
    
    // Find All
    public ServiceResponse findAll() {
        ServiceResponse result = new ServiceResponse();
        result.setMessage("success");
        result.setData(customerRepo.findAll());
        
        return result;
    }
    
    public ServiceResponse sendMails() {
        
        ServiceResponse result = new ServiceResponse();
        result.setMessage("success");
        
        List<Customer> customers = customerRepo.findAll();
        
        List<String> emails = new ArrayList<>();
        
        String message = ""
        
        for (Customer c : customers) {
            emails.add(c.getEmail());
        }
        
        try {
            sendEmail(emails);
        } catch (Exception e) {
            if (e instanceof MessagingException) {
                result.setData("There are message exception occured.");
            }
        }
        
        result.setData("");
        
        return result;
    }
    
    public void sendEmail(List<String> emails) throws MessagingException {
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
        
        Transport transport = session.getTransport();
        transport.connect();
        
        for (String email : emails) {
            try {
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
                
                transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        transport.close();
    }
    
    // Find All Emails
    public ServiceResponse findAllEmails() {
        ServiceResponse result = new ServiceResponse();
        
        result.setMessage("success");
        
        List<Customer> customers = customerRepo.findAll();
        
        List<String> emails = new ArrayList<>();
        
        for (Customer c : customers) {
            emails.add(c.getEmail());
        }
        result.setData(emails);
        
        return result;
    }
    
    public ServiceResponse findById(String id) {
        ServiceResponse result = new ServiceResponse();
        Customer customer = customerRepo.findById(id).orElse(null);
        result.setData(customer);
        result.setMessage("success");
        
        return result;
    }
    
    public ServiceResponse findById1(String id) {
        ServiceResponse result = new ServiceResponse();
        Customer customer = customerRepo.findById(id).orElse(null);
        result.setMessage("success");
        result.setData(customer);
        
        return result;
    }
    
    public ServiceResponse create(Customer customer) {
        ServiceResponse result = new ServiceResponse();
        result.setMessage("success");
        result.setData(customerRepo.save(customer));
        
        return result;
    }
    
    public ServiceResponse update(Customer customer) {
        ServiceResponse result = new ServiceResponse();
        
        if (!customerRepo.findById(customer.getId()).isPresent()) {
            result.setStatus(ServiceResponse.Status.FAILED);
            result.setMessage("Customer not found.");
        } else {
            result.setData(customerRepo.save(customer));
        }
        return result;
    }
    
    public ServiceResponse delete(String id) {
        ServiceResponse result = new ServiceResponse();
        Customer customer = customerRepo.findById(id).orElse(null);
        
        if (customer == null) {
            result.setStatus(ServiceResponse.Status.FAILED);
            result.setMessage("Customer not found.");
        } else {
            customerRepo.delete(customer);
            result.setMessage("success");
        }
        return result;
    }
}
