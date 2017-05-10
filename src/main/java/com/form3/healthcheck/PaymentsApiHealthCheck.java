package com.form3.healthcheck;

import com.codahale.metrics.health.HealthCheck;

public class PaymentsApiHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy("payments-api is up and running");
    }
}
