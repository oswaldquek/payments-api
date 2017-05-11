package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.db.MockPaymentsDataService;
import com.form3.domain.Payment;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.form3.TestHelpers.createPayment;
import static org.assertj.core.api.Assertions.assertThat;

public class CreatePaymentsTest {

    CreatePayments createPayments = new CreatePayments(new MockPaymentsDataService());

    @Test
    public void createSuccessfulPayment() {
        Payment payment = createPayment();
        Response response = createPayments.create(null, payment);
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getLocation().toString()).isNotEmpty();
        assertThat(response.getEntity()).isEqualToIgnoringGivenFields(payment, "id");
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }
}
