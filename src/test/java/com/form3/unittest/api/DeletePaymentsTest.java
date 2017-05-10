package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.DeletePayments;
import com.form3.api.GetPayments;
import com.form3.db.PaymentsDataService;
import com.form3.db.PaymentsDataServiceImpl;
import com.form3.domain.Payment;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Set;

import static com.form3.TestHelpers.createPayment;
import static com.form3.TestHelpers.getLink;
import static org.assertj.core.api.Assertions.assertThat;

public class DeletePaymentsTest {

    private static PaymentsDataService db = new PaymentsDataServiceImpl();

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new GetPayments(db))
            .addResource(new CreatePayments(db))
            .addResource(new DeletePayments(db))
            .build();

    @Test
    public void shouldReturnOkIfDeletingANonExistentPayment() {
        Response response = RESOURCES.client().target("/payments/delete/non-existent-id")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void paymentShouldBeDeleted() {
        Payment payment = createPayment();
        Set<Link> links = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payment)).getLinks();
        Response response = RESOURCES.client().target(getLink(links, "delete"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response = RESOURCES.client().target(getLink(links, "self"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat(response.getLinks()).isEmpty();
    }
}
