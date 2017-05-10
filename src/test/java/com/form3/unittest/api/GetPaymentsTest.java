package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.GetPayments;
import com.form3.db.PaymentsDataService;
import com.form3.db.PaymentsDataServiceImpl;
import com.form3.domain.Payment;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collection;
import java.util.Currency;

import static com.form3.TestHelpers.createPayment;
import static org.assertj.core.api.Assertions.assertThat;

public class GetPaymentsTest {

    private static PaymentsDataService db = new PaymentsDataServiceImpl();

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new GetPayments(db))
            .addResource(new CreatePayments(db))
            .build();

    @Before
    public void before() {
        ((PaymentsDataServiceImpl) db).clear();
    }

    @Test
    public void createAndGetSinglePayment() {
        Payment payment = createPayment();
        URI uri = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payment)).getLocation();
        Response response = RESOURCES.client().target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(Payment.class)).isEqualToIgnoringGivenFields(payment, "id");
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }

    @Test
    public void getNonExistentPaymentShouldReturnNotFound() {
        Response response = RESOURCES.client().target("/payments/does-not-exist").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void createAndGetPayments() {
        Payment payment1 = createAndGetPayment(new BigDecimal("50"), Currency.getInstance("USD"));
        Payment payment2 = createAndGetPayment(new BigDecimal("110"), Currency.getInstance("GBP"));
        Response response = RESOURCES.client().target("/payments/all").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(new GenericType<Collection<Payment>>() {})).containsOnly(payment1, payment2);
    }

    private Payment createAndGetPayment(BigDecimal amount, Currency currency) {
        Payment payment = createPayment(amount, currency);
        URI uri = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payment)).getLocation();
        return RESOURCES.client().target(uri).request(MediaType.APPLICATION_JSON_TYPE).get().readEntity(Payment.class);
    }

}
