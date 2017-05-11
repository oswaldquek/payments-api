package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.GetPayments;
import com.form3.api.UpdatePayments;
import com.form3.db.MockPaymentsDataService;
import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;
import com.form3.domain.PaymentAttributes;
import com.form3.domain.PaymentStatus;
import com.form3.domain.builders.PaymentAttributesBuilder;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static com.form3.TestHelpers.createPayment;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePaymentsTest {

    private static PaymentsDataService db = new MockPaymentsDataService();

    GetPayments getPayments = new GetPayments(db);
    CreatePayments createPayments = new CreatePayments(db);
    UpdatePayments updatePayments = new UpdatePayments(db);

    @Test
    public void updateSuccessfully() {
        Payment payment = (Payment) createPayments.create(null, createPayment()).getEntity();
        PaymentAttributes newAttributes = PaymentAttributesBuilder.copyFrom(payment.getAttributes()).setCurrency(Currency.getInstance("USD")).build();
        Payment newPayment = new Payment(payment.getType(), payment.getId().get(), newAttributes);
        Response response = updatePayments.update(null, payment.getId().get(), newPayment);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(getPayments.getPayment(null, payment.getId().get()).getEntity()).isEqualTo(newPayment);
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }

    @Test
    public void shouldReturnNotFoundIfIdDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Payment payment = createPayment().createWithId(id);
        Response response = updatePayments.update(null, id, payment);
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat((List<String>) response.getEntity()).containsExactly(format("Payment with id %s does not exist", id));
    }

    @Test
    public void shouldReturnErrorsIfAttemptingToUpdateImmutableFields() {
        Payment payment = (Payment) createPayments.create(null, createPayment()).getEntity();
        PaymentAttributes newAttributes = PaymentAttributesBuilder.copyFrom(payment.getAttributes())
                .setStatus(PaymentStatus.PROCESSED)
                .build();
        Payment newPayment = new Payment(payment.getType(), "new-id", newAttributes);
        Response response = updatePayments.update(null, payment.getId().get(), newPayment);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat((List<String>) response.getEntity())
                .containsOnly("Updating payment status is not allowed", "Updating the id is not allowed");
    }
}
