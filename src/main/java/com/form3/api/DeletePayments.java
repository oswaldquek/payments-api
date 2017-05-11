package com.form3.api;

import com.form3.auth.User;
import com.form3.db.PaymentsDataService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.security.Principal;

@Path("/payments/delete")
public class DeletePayments {
    private PaymentsDataService db;

    public DeletePayments(PaymentsDataService db) {
        this.db = db;
    }

    @DELETE
    @Path("/{paymentId}")
    public Response delete(@Auth User user, @PathParam("paymentId") String id) {
        db.delete(id);
        return Response.ok().build();
    }
}
