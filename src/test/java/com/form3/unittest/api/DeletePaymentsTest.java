package com.form3.unittest.api;

import com.form3.api.CreatePayments;
import com.form3.api.DeletePayments;
import com.form3.api.GetPayments;
import com.form3.db.MockPaymentsDataService;
import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.form3.TestHelpers.createPayment;
import static org.assertj.core.api.Assertions.assertThat;

public class DeletePaymentsTest {

    private static PaymentsDataService db = new MockPaymentsDataService();

    GetPayments getPayments = new GetPayments(db);
    CreatePayments createPayments = new CreatePayments(db);
    DeletePayments deletePayments = new DeletePayments(db);

    @Test
    public void shouldReturnOkIfDeletingANonExistentPayment() {
        Response response = deletePayments.delete(null, "non-existent-id");
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void paymentShouldBeDeleted() {
        Payment payment = createPayment();
        Payment paymentWithId = (Payment) createPayments.create(null, payment).getEntity();
        Response response = deletePayments.delete(null, paymentWithId.getId().get());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response = getPayments.getPayment(null, paymentWithId.getId().get());
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat(response.getLinks()).isEmpty();
    }
}
