package com.form3;

import com.form3.api.CreatePayments;
import com.form3.api.DeletePayments;
import com.form3.api.LoginResource;
import com.form3.api.GetPayments;
import com.form3.api.UpdatePayments;
import com.form3.auth.MockSecretsManagementService;
import com.form3.auth.MockUserValidationService;
import com.form3.auth.PaymentsAuthenticator;
import com.form3.auth.SecretsManagementService;
import com.form3.auth.User;
import com.form3.auth.UserValidationService;
import com.form3.db.PaymentsDataService;
import com.form3.db.MockPaymentsDataService;
import com.form3.healthcheck.PaymentsApiHealthCheck;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

public class PaymentsApplication extends Application<PaymentsApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new PaymentsApplication().run(args);
    }

    @Override
    public void run(PaymentsApplicationConfiguration configuration, Environment environment) throws Exception {
        registerResources(environment);
        registerHealthCheck(environment);
        setUpAuthJwt(configuration, environment);
    }

    private void registerHealthCheck(Environment environment) {
        environment.healthChecks().register("payments-api", new PaymentsApiHealthCheck());
    }

    private void registerResources(Environment environment) {
        PaymentsDataService db = new MockPaymentsDataService();
        environment.jersey().register(new CreatePayments(db));
        environment.jersey().register(new GetPayments(db));
        environment.jersey().register(new UpdatePayments(db));
        environment.jersey().register(new DeletePayments(db));
    }

    protected void setUpAuthJwt(PaymentsApplicationConfiguration configuration, Environment environment) throws UnsupportedEncodingException {
        SecretsManagementService secretsManagementService = new MockSecretsManagementService();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(secretsManagementService.getJwtTokenSecret())) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(new PaymentsAuthenticator())
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        UserValidationService userValidationService = new MockUserValidationService();
        environment.jersey().register(new LoginResource(secretsManagementService, userValidationService, configuration.getJwtTokenValidity()));
    }
}
