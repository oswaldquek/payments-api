package com.form3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;

public class Party {
    @NotNull
    @JsonProperty("account_number")
    private String accountNumber;
    @NotNull
    @JsonProperty("account_name")
    private String accountName;
    @NotNull
    @JsonProperty("bank_id")
    private String bankId;
    @NotNull
    @JsonProperty("bank_id_code")
    private String bankIdCode;

    private Party() {}

    public Party(String accountNumber, String accountName, String bankId, String bankIdCode) {

        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.bankId = bankId;
        this.bankIdCode = bankIdCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankIdCode() {
        return bankIdCode;
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
