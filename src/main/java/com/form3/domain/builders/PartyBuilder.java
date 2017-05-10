package com.form3.domain.builders;

import com.form3.domain.Party;

public class PartyBuilder {
    private String accountNumber;
    private String accountName;
    private String bankId;
    private String bankIdCode;

    public PartyBuilder setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public PartyBuilder setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public PartyBuilder setBankId(String bankId) {
        this.bankId = bankId;
        return this;
    }

    public PartyBuilder setBankIdCode(String bankIdCode) {
        this.bankIdCode = bankIdCode;
        return this;
    }

    public Party build() {
        return new Party(accountNumber, accountName, bankId, bankIdCode);
    }
}
