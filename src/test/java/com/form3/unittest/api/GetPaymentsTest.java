package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.GetPayments;
import com.form3.db.MockPaymentsDataService;
import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;

import static com.form3.TestHelpers.createPayment;
import static org.assertj.core.api.Assertions.assertThat;

public class GetPaymentsTest {

    private static PaymentsDataService db = new MockPaymentsDataService();

    GetPayments getPayments = new GetPayments(db);
    CreatePayments createPayments = new CreatePayments(db);

    @Before
    public void before() {
        ((MockPaymentsDataService) db).clear();
    }

    @Test
    public void createAndGetSinglePayment() {
        Payment payment = createPayment();
        Payment paymentWithId = (Payment) createPayments.create(null, payment).getEntity();
        Response response = getPayments.getPayment(null, paymentWithId.getId().get());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(paymentWithId);
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }

    @Test
    public void getNonExistentPaymentShouldReturnNotFound() {
        Response response = getPayments.getPayment(null, "does-not-exist-id");
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void createAndGetPayments() {
        Payment payment1 = createAndGetPayment(new BigDecimal("50"), Currency.getInstance("USD"));
        Payment payment2 = createAndGetPayment(new BigDecimal("110"), Currency.getInstance("GBP"));
        Response response = getPayments.getAllPayments(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat((Collection<Payment>) response.getEntity()).containsOnly(payment1, payment2);
    }

    private Payment createAndGetPayment(BigDecimal amount, Currency currency) {
        Payment payment = createPayment(amount, currency);
        return (Payment) createPayments.create(null, payment).getEntity();
    }

}
