package com.example.demo.controller;

import com.example.demo.response.ServiceResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class HandleErrorController implements ErrorController {
    
    /**
     * Generally handle error for APIs
     *
     * @param request
     * @return
     */
    @RequestMapping("/error")
    public ResponseEntity<ServiceResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        ServiceResponse result = new ServiceResponse();
        result.setData(null);
        result.setMessage("Default error message");
        
        ResponseEntity<ServiceResponse> responseEntity = new ResponseEntity<>(result, HttpStatus.MULTI_STATUS);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                
                result.setStatus(ServiceResponse.Status.NOT_FOUND);
                result.setMessage("Not Found Error (from Gaunau)");
                result.setData(null);
                
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                result.setStatus(ServiceResponse.Status.INTERNAL_ERROR);
                result.setMessage("Internal Server Error. (from Gaunau)");
                result.setData(null);
                
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Handle error type
        return responseEntity;
    }
    
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
