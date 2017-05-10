package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.GetPayments;
import com.form3.api.UpdatePayments;
import com.form3.db.PaymentsDataService;
import com.form3.db.PaymentsDataServiceImpl;
import com.form3.domain.Payment;
import com.form3.domain.PaymentAttributes;
import com.form3.domain.PaymentStatus;
import com.form3.domain.builders.PaymentAttributesBuilder;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static com.form3.TestHelpers.createPayment;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePaymentsTest {

    private static PaymentsDataService db = new PaymentsDataServiceImpl();

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new GetPayments(db))
            .addResource(new CreatePayments(db))
            .addResource(new UpdatePayments(db))
            .build();

    @Test
    public void updateSuccessfully() {
        URI linkToPayment = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createPayment())).getLocation();
        Payment payment = RESOURCES.client().target(linkToPayment)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Payment.class);
        PaymentAttributes newAttributes = PaymentAttributesBuilder.copyFrom(payment.getAttributes()).setCurrency(Currency.getInstance("USD")).build();
        Payment newPayment = new Payment(payment.getType(), payment.getId().get(), newAttributes);
        Response response = RESOURCES.client().target("/payments/update/" + payment.getId().get())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(newPayment));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(RESOURCES.client().target(linkToPayment)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Payment.class)).isEqualTo(newPayment);
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }

    @Test
    public void shouldReturnNotFoundIfIdDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Payment payment = createPayment().createWithId(id);
        Response response = RESOURCES.client().target("/payments/update/" + id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(payment));
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat(response.readEntity(new GenericType<List<String>>() {}))
                .containsExactly(format("Payment with id %s does not exist", id));
    }

    @Test
    public void shouldReturnErrorsIfAttemptingToUpdateImmutableFields() {
        URI linkToPayment = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createPayment())).getLocation();
        Payment payment = RESOURCES.client().target(linkToPayment)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Payment.class);
        PaymentAttributes newAttributes = PaymentAttributesBuilder.copyFrom(payment.getAttributes())
                .setStatus(PaymentStatus.PROCESSED)
                .build();
        Payment newPayment = new Payment(payment.getType(), payment.getId().get(), newAttributes);
        Response response = RESOURCES.client().target("/payments/update/" + payment.getId().get())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(newPayment));
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(new GenericType<List<String>>() {}))
                .containsExactly("Cannot update immutable value payment status");
    }

    @Test
    public void shouldReturnBadRequestIfPaymentIdAndPaymentDoNotMatch() {
        URI linkToPayment = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createPayment())).getLocation();
        Payment payment = RESOURCES.client().target(linkToPayment)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Payment.class);
        Response response = RESOURCES.client().target("/payments/update/" + payment.getId().get())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(new Payment(payment.getType(), "new-id", payment.getAttributes())));
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(new GenericType<List<String>>() {}))
                .containsExactly(format("Attempted to update payment with id %s, but the id contained in the post body was %s", payment.getId().get(), "new-id"));
    }
}
