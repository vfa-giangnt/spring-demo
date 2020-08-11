package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.response.ServiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    
    @Autowired
    CustomerRepository customerRepo;
    
    // Find All
    public ServiceResponse findAll() {
        ServiceResponse result = new ServiceResponse();
        result.setData(customerRepo.findAll());
        
        return result;
    }
    
    public ServiceResponse findById(int id) {
        ServiceResponse result = new ServiceResponse();
        Customer customer = customerRepo.findById(id).orElse(null);
        result.setData(customer);
        
        return result;
    }
    
    public ServiceResponse findById1(int id) {
        ServiceResponse result = new ServiceResponse();
        Customer customer = customerRepo.findById(id).orElse(null);
        result.setData(customer);
        
        return result;
    }
    
    public ServiceResponse create(Customer customer) {
        ServiceResponse result = new ServiceResponse();
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
    
    public ServiceResponse delete(int id) {
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
