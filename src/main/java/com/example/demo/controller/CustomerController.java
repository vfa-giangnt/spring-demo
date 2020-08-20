package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.request.DeleteCustomerRequest;
import com.example.demo.request.FindCustomerRequest;
import com.example.demo.response.ServiceResponse;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/customers")
    public ResponseEntity<ServiceResponse> findAllCustomer() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }
    
    /* new - processing */
    @GetMapping("/send-mails")
    public ResponseEntity<ServiceResponse> sendMails() {
        return new ResponseEntity<>(customerService.sendMails(), HttpStatus.OK);
    }
    
    @GetMapping("/mails")
    public ResponseEntity<ServiceResponse> findAllMail() {
        return new ResponseEntity<>(customerService.findAllEmails(), HttpStatus.OK);
    }
    
    /* ---------------- GET CUSTOMER BY ID ------------------------ */
    @GetMapping("/customers/{id}")
    public ResponseEntity<ServiceResponse> findById(@PathVariable String id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }
    
    @GetMapping("/customers/search")
    public ResponseEntity<ServiceResponse> findById1(@RequestBody FindCustomerRequest request) {
        return new ResponseEntity<>(customerService.findById(request.getId()), HttpStatus.OK);
    }
    
    /* ---------------- CREATE NEW CUSTOMER ------------------------ */
    @PostMapping("/customers")
    public ResponseEntity<ServiceResponse> create(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.create(customer), HttpStatus.OK);
    }
    
    /* ---------------- UPDATE CUSTOMER ------------------------ */
    @PutMapping("/customers")
    public ResponseEntity<ServiceResponse> update(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.update(customer), HttpStatus.OK);
    }
    
    @DeleteMapping("/customers")
    public ResponseEntity<ServiceResponse> delete(@RequestBody DeleteCustomerRequest request) {
        return new ResponseEntity<>(customerService.delete(request.getId()), HttpStatus.OK);
    }
}
