package com.form3.integration;

import com.form3.PaymentsApplication;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.testing.ResourceHelpers;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationTest {

    private Client client = JerseyClientBuilder.createClient();

    @ClassRule
    public static PaymentsApiAppRule paymentsApi = new PaymentsApiAppRule(
            PaymentsApplication.class,
            ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    public void returnUnathorizedWhenUserDoesNotExist() {
        Map<String, String> user = ImmutableMap.of("user", "Unknown", "password", "p4ssw0rd");
        Response response = client.target(paymentsApi.uri("/payments/login")).request().post(Entity.json(user));
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void returnUnathorizedWhenPasswordIsIncorrect() {
        Map<String, String> user = ImmutableMap.of("user", "Joe Bloggs", "password", "password");
        Response response = client.target(paymentsApi.uri("/payments/login")).request().post(Entity.json(user));
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void canAccessResourceWithToken() {
        Map<String, String> user = ImmutableMap.of("user", "Joe Bloggs", "password", "p4ssw0rd");
        Response response = client.target(paymentsApi.uri("/payments/login")).request().post(Entity.json(user));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        String token = (String) response.readEntity(Map.class).get("token");
        response = client.target(paymentsApi.uri("/payments/delete/some-id")).request()
                .header("Authorization", "Bearer " + token).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void cannotAccessResourceWithInvalidToken() {
        Response response = client.target(paymentsApi.uri("/payments/delete/some-id")).request()
                .header("Authorization", "Bearer INVALID-TOKEN").delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void cannotAccessResourceWithExpiredToken() throws InterruptedException {
        Map<String, String> user = ImmutableMap.of("user", "Joe Bloggs", "password", "p4ssw0rd");
        String token = (String) client.target(paymentsApi.uri("/payments/login")).request().post(Entity.json(user)).readEntity(Map.class).get("token");
        Response response = client.target(paymentsApi.uri("/payments/delete/some-id")).request()
                .header("Authorization", "Bearer " + token).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        Thread.sleep(5500);

        response = client.target(paymentsApi.uri("/payments/delete/some-id")).request()
                .header("Authorization", "Bearer " + token).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void cannotAccessResourceWithNoToken() {
        Response response = client.target(paymentsApi.uri("/payments/delete/some-id")).request().delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
