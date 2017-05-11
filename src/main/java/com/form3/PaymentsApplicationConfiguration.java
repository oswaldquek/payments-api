package com.form3;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PaymentsApplicationConfiguration extends Configuration {

    @Valid
    @JsonProperty
    @NotNull
    private int jwtTokenValiditySeconds;

    public int getJwtTokenValidity() {
        return jwtTokenValiditySeconds;
    }
}
