package com.form3.integration;

import com.form3.domain.Payment;
import io.dropwizard.testing.ResourceHelpers;
import org.apache.commons.io.FileUtils;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

import static com.form3.TestHelpers.getInvocationBuilder;
import static com.form3.TestHelpers.getJerseyClient;
import static org.assertj.core.api.Assertions.assertThat;

public class CreatePaymentsTest {

    private Client client = getJerseyClient();

    @ClassRule
    public static PaymentsApiAppRule paymentsApi = new PaymentsApiAppRule(
            PaymentsApplicationIntegrationTest.class,
            ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    public void createPayment() throws IOException {
        String json = FileUtils.readFileToString(new File(ResourceHelpers.resourceFilePath("valid-create-payment.json")), "UTF-8");
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/create")).post(Entity.json(json));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.readEntity(Payment.class)).isNotNull();
    }

    @Test
    public void createPaymentWithMissingType_paymentType_bankId_shouldReturnUnprocessableEntity() throws IOException {
        String json = FileUtils.readFileToString(new File(ResourceHelpers.resourceFilePath("payment-with-missing-fields.json")), "UTF-8");
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/create")).post(Entity.json(json));
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createPaymentWithInvalidPaymentScheme_shouldReturnBadRequest() throws IOException {
        String json = FileUtils.readFileToString(new File(ResourceHelpers.resourceFilePath("payment-with-invalid-scheme.json")), "UTF-8");
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/create")).post(Entity.json(json));
        assertThat(response.getStatus()).isEqualTo(400);
    }

}
