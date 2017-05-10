package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.db.PaymentsDataService;
import com.form3.db.PaymentsDataServiceImpl;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.form3.TestHelpers.createPayment;
import static org.assertj.core.api.Assertions.assertThat;

public class CreatePaymentsTest {

    private static PaymentsDataService db = new PaymentsDataServiceImpl();

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new CreatePayments(db))
            .build();

    @Test
    public void createSuccessfulPayment() {
        Response response = RESOURCES.client().target("/payments/create").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createPayment()));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getLocation().toString()).isNotEmpty();
        assertThat(response.hasLink("self")).isTrue();
        assertThat(response.hasLink("update")).isTrue();
        assertThat(response.hasLink("delete")).isTrue();
    }
}
