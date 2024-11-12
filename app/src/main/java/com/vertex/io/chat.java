package com.vertex.io;

public class chat {
    String yourMessage;
    String adminMessage;

    public chat() {
    }
    public chat(String yourMessage, String adminMessage) {
        this.yourMessage = yourMessage;
        this.adminMessage = adminMessage;
    }
    public String getYourMessage() {
        return yourMessage;
    }
    public void setYourMessage(String yourMessage) {
        this.yourMessage = yourMessage;
    }
    public String getAdminMessage() {
        return adminMessage;
    }
    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }
}
