package com.vertex.io;

public class Tickets {

    private String ticketID;
    private String date;
    private String status;
    private String subject;
    private String description;

    public Tickets() {
        this.ticketID = "Not Available";
        this.date = "Not Available";
        this.status = "Not Available";
        this.subject = "Not Available";
        this.description = "Not Available";
    }

    public Tickets(String ticketID, String date, String status, String subject, String description) {
        this.ticketID = ticketID != null ? ticketID : "Not Available";
        this.date = date != null ? date : "Not Available";
        this.status = status != null ? status : "Not Available";
        this.subject = subject != null ? subject : "Not Available";
        this.description = description != null ? description : "Not Available";
    }

    public String getTicketID() {
        return ticketID != null ? ticketID : "Not Available";
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getDate() {
        return date != null ? date : "Not Available";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status != null ? status : "Not Available";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject != null ? subject : "Not Available";
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description != null ? description : "Not Available";
    }

    public void setDescription(String description) {
        this.description = description;
    }
}