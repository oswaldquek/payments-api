package com.form3.api;

import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

import static com.form3.helpers.LinksHelper.getLinks;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
public class GetPayments {
    private final PaymentsDataService db;

    public GetPayments(PaymentsDataService db) {
        this.db = db;
    }

    @GET
    @Path("/{paymentId}")
    public Response getPayment(@PathParam("paymentId") String id) {
        Optional<Payment> payment = db.get(id);
        if (payment.isPresent()) {
            return Response.ok(payment.get()).links(getLinks(id)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/all")
    public Response getAllPayments() {
        Collection<Payment> payments = db.getAll();
        return Response.ok().entity(payments).build();
    }
}
