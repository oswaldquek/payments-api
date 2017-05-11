package com.form3.integration;

import com.form3.domain.Payment;
import com.form3.domain.PaymentAttributes;
import com.form3.domain.builders.PaymentAttributesBuilder;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.util.Currency;

import static com.form3.TestHelpers.createPayment;
import static com.form3.TestHelpers.getInvocationBuilder;
import static com.form3.TestHelpers.getJerseyClient;
import static com.form3.TestHelpers.getLink;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePaymentTest {

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
    public void updatePayment() {
        PaymentAttributes newAttributes = PaymentAttributesBuilder.copyFrom(payment.getAttributes()).setCurrency(Currency.getInstance("USD")).build();
        Payment newPayment = new Payment(payment.getType(), payment.getId().get(), newAttributes);
        Response response = getInvocationBuilder(client, paymentsApi.uri("/payments/update/" + this.payment.getId().get())).put(Entity.json(newPayment));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(getInvocationBuilder(client, getLink(response.getLinks(), "self").getUri()).get().readEntity(Payment.class)).isEqualTo(newPayment);
    }
}
