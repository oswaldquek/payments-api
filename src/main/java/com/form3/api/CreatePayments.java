package com.form3.api;

import com.form3.auth.User;
import com.form3.db.PaymentsDataService;
import com.form3.domain.Payment;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.security.Principal;

import static com.form3.helpers.LinksHelper.getLinks;

@Path("/payments/create")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreatePayments {

    private PaymentsDataService db;

    public CreatePayments(PaymentsDataService db) {
        this.db = db;
    }

    @POST
    public Response create(@Auth User user, @Valid Payment payment) {
        if (payment == null) throw new RuntimeException("payment is null");
        String id = db.store(payment);
        URI location = UriBuilder.fromPath("/payments/{id}").build(id);
        return Response.created(location).entity(db.get(id).get()).links(getLinks(id)).build();
    }
}
