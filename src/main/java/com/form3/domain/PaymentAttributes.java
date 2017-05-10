package com.form3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

public class PaymentAttributes {

    @NotNull
    private PaymentStatus status;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
    @NotNull
    @JsonProperty("payment_type")
    private PaymentType type;
    @NotNull
    @JsonProperty("payment_scheme")
    private PaymentScheme scheme;
    @NotNull
    @JsonProperty("originating_party")
    private Party originatingParty;
    @NotNull
    @JsonProperty("beneficiary_party")
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
