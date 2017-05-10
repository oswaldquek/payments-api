package com.form3.domain;

public enum PaymentType {

    CREDIT("Credit"), DEBIT("Debit");

    public final String type;

    PaymentType(String type) {
        this.type = type;
    }
}
