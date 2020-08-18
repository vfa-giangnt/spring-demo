package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.response.ServiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
