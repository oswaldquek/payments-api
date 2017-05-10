package com.form3.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class Payment {
    @NotNull
    private Type type;
    private Optional<String> id = Optional.empty();
    @NotNull
    private PaymentAttributes attributes;

    private Payment() {}

    public Payment(Type type, PaymentAttributes attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    public Payment(Type type, String id, PaymentAttributes attributes) {
        this.type = type;
        this.id = Optional.of(id);
        this.attributes = attributes;
    }

    public Type getType() {
        return type;
    }

    public PaymentAttributes getAttributes() {
        return attributes;
    }

    public Optional<String> getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public Payment createWithId(String id) {
        return new Payment(this.type, id, this.attributes);
    }
}
