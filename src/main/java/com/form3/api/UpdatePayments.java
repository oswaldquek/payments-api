package com.form3.api;

import com.form3.auth.User;
import com.form3.domain.Errors;
import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Optional;

import static com.form3.helpers.LinksHelper.getLinks;

@Path("/payments/update")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UpdatePayments {
    private PaymentsDataService db;

    public UpdatePayments(PaymentsDataService db) {
        this.db = db;
    }

    @PUT
    @Path("/{paymentId}")
    public Response update(@Auth User user, @PathParam("paymentId") String id, @Valid Payment payment) {
        Optional<Errors> errors = db.update(id, payment);
        if (errors.isPresent()) {
            return Response.status(errors.get().getCode().status).entity(errors.get().getMessages()).build();
        }
        return Response.ok().links(getLinks(id)).build();
    }
}
