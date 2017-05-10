package com.form3.api;

import com.form3.db.PaymentsDataService;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/payments/delete")
public class DeletePayments {
    private PaymentsDataService db;

    public DeletePayments(PaymentsDataService db) {
        this.db = db;
    }

    @DELETE
    @Path("/{paymentId}")
    public Response getPayment(@PathParam("paymentId") String id) {
        db.delete(id);
        return Response.ok().build();
    }
}
