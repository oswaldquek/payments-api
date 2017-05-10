package com.form3.domain.builders;

import com.form3.domain.Party;
import com.form3.domain.PaymentAttributes;
import com.form3.domain.PaymentScheme;
import com.form3.domain.PaymentStatus;
import com.form3.domain.PaymentType;

import java.math.BigDecimal;
import java.util.Currency;

public class PaymentAttributesBuilder {
    private PaymentStatus status;
    private BigDecimal amount;
    private Currency currency;
    private PaymentType type;
    private PaymentScheme scheme;
    private Party originatingParty;
    private Party beneficiaryParty;

    public PaymentAttributesBuilder setStatus(PaymentStatus status) {
        this.status = status;
        return this;
    }

    public PaymentAttributesBuilder setAmount(BigDecimal bigDecimal) {
        amount = bigDecimal;
        return this;
    }

    public PaymentAttributesBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public PaymentAttributesBuilder setPaymentType(PaymentType type) {
        this.type = type;
        return this;
    }

    public PaymentAttributesBuilder setPaymentScheme(PaymentScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public PaymentAttributesBuilder setOriginatingParty(Party originatingParty) {
        this.originatingParty = originatingParty;
        return this;
    }

    public PaymentAttributesBuilder setBeneficiaryParty(Party beneficiaryParty) {
        this.beneficiaryParty = beneficiaryParty;
        return this;
    }

    public PaymentAttributes build() {
        return new PaymentAttributes(status, amount, currency, type, scheme, originatingParty, beneficiaryParty);
    }

    public static PaymentAttributesBuilder copyFrom(PaymentAttributes attributes) {
        PaymentAttributesBuilder builder = new PaymentAttributesBuilder();
        builder.setAmount(attributes.getAmount());
        builder.setBeneficiaryParty(attributes.getBeneficiaryParty());
        builder.setCurrency(attributes.getCurrency());
        builder.setOriginatingParty(attributes.getOriginatingParty());
        builder.setPaymentScheme(attributes.getScheme());
        builder.setPaymentType(attributes.getType());
        builder.setStatus(attributes.getStatus());
        return builder;
    }
}
