package com.form3.integration;

import com.form3.domain.Payment;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static com.form3.TestHelpers.createPayment;
import static com.form3.TestHelpers.getInvocationBuilder;
import static com.form3.TestHelpers.getJerseyClient;
import static org.assertj.core.api.Assertions.assertThat;

public class GetPaymentsTest {

    private Client client = getJerseyClient();

    private Payment payment;

    @ClassRule
    public static PaymentsApiAppRule paymentsApi = new PaymentsApiAppRule(
            PaymentsApplicationIntegrationTest.class,
            ResourceHelpers.resourceFilePath("config.yml"));

    @Before
    public void setupPayment() {
        payment = getInvocationBuilder(client, paymentsApi.uri("/payments/create")).post(Entity.json(createPayment())).readEntity(Payment.class);
    }

    @Test
    public void getSinglePaymentTest() {
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/" + payment.getId().get())).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void getAllPaymentsTest() {
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/all")).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}
