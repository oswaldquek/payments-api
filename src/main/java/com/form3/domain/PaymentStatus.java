package com.form3.domain;

public enum PaymentStatus {

    SUBMITTED("Submitted"), PROCESSED("Processed");

    public final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
