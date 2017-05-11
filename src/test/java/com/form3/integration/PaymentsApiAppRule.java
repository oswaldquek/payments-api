package com.form3.integration;

import com.form3.PaymentsApplication;
import com.form3.PaymentsApplicationConfiguration;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class PaymentsApiAppRule extends DropwizardAppRule<PaymentsApplicationConfiguration> {
    public PaymentsApiAppRule(Class<? extends PaymentsApplication> applicationClass, String configPath) {
        super(applicationClass, configPath);
    }

    public URI uri(String path) {
        return UriBuilder.fromUri("http://localhost")
                .path(path)
                .port(getLocalPort())
                .build();
    }
}
