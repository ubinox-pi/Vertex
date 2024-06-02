package com.vertex.io;

public class withdraw_class {
    private String id;
    private String upiId;
    private double amount;
    private String status;
    private String date;
    private String txnId;

    public withdraw_class() {
        // Default constructor required for calls to DataSnapshot.getValue(Withdrawal.class)
    }

    public withdraw_class(String id, String upiId, double amount, String status, String txnId, String date) {
        this.id = id;
        this.upiId = upiId;
        this.amount = amount;
        this.status = status;
        this.txnId = txnId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUpiId() {
        return upiId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
    public String getDate() {
        return date;
    }

    public String getTxnId() {
        return txnId;
    }
}

