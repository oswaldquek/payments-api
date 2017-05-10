package com.form3.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.Currency;

public class PaymentAttributes {

    private PaymentStatus status;
    private BigDecimal amount;
    private Currency currency;
    private PaymentType type;
    private PaymentScheme scheme;
    private Party originatingParty;
    private Party beneficiaryParty;

    private PaymentAttributes() {}

    public PaymentAttributes(PaymentStatus status,
                             BigDecimal amount,
                             Currency currency,
                             PaymentType type,
                             PaymentScheme scheme,
                             Party originatingParty,
                             Party beneficiaryParty) {

        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.scheme = scheme;
        this.originatingParty = originatingParty;
        this.beneficiaryParty = beneficiaryParty;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaymentType getType() {
        return type;
    }

    public PaymentScheme getScheme() {
        return scheme;
    }

    public Party getOriginatingParty() {
        return originatingParty;
    }

    public Party getBeneficiaryParty() {
        return beneficiaryParty;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
