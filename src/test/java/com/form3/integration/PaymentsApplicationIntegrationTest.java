package com.form3.integration;

import com.form3.PaymentsApplication;
import com.form3.PaymentsApplicationConfiguration;
import com.form3.auth.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class PaymentsApplicationIntegrationTest extends PaymentsApplication {

    // override setUpAuthJwt so we don't have to get an access token for each integration test. this means
    // a header of "Authorization Basic cGVlc2tpbGxldDpwYXNz" for example needs to be set on each request
    @Override
    protected void setUpAuthJwt(PaymentsApplicationConfiguration configuration, Environment environment) throws UnsupportedEncodingException {
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new ExampleAuthenticator())
                        .setAuthorizer(new ExampleAuthorizer())
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {
        @Override
        public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
            return Optional.of(new User("dummy-id","dummy-name"));
        }
    }

    public class ExampleAuthorizer implements Authorizer<User> {
        @Override
        public boolean authorize(User user, String role) {
            return true;
        }
    }
}
