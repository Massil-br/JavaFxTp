package com.massil;

import java.time.LocalDate;

public class SupportTicket {
    private long id;
    public long getId() {
        return id;
    }

    private String title;
    public String getTitle() {
        return title;
    }

    private String customerName;
    public String getCustomerName() {
        return customerName;
    }

    private String priority;
    public String getPriority() {
        return priority;
    }

    private LocalDate createdAt;
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    private String description;
    public String getDescription() {
        return description;
    }

    private boolean urgent;
    public boolean isUrgent() {
        return urgent;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public SupportTicket(long id, String title, String customerName, String priority, LocalDate createdAt, String description, boolean urgent, String status){
        this.id = id;
        this.title = title;
        this.customerName = customerName;
        this.priority = priority;
        this.createdAt = createdAt;
        this.description = description;
        this.urgent = urgent;
        this.status = status;
    }

    public SupportTicket(String title, String customerName, String priority, LocalDate createdAt, String description, boolean urgent, String status){
        this.title = title;
        this.customerName = customerName;
        this.priority = priority;
        this.createdAt = createdAt;
        this.description = description;
        this.urgent = urgent;
        this.status = status;
    }

    @Override
    public String toString() {
        return "SupportTicket{id="+id+", title='"+title+"', customerName='"+customerName+"', priority='"+priority+"', createdAt="+createdAt+", description='"+description+"', urgent="+urgent+", status='"+status+"'}";
    }

    public SupportTicket withId(long id) {
        return new SupportTicket(id, title, customerName, priority, createdAt, description, urgent, status);
    }
    

}
