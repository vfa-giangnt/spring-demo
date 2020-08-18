package com.example.demo.response;

public class ServiceResponse {
    private Status status = Status.SUCCESS;
    private String message;
    private Object data;
    
    public enum Status {
        SUCCESS, FAILED, INTERNAL_ERROR, NOT_FOUND;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}
