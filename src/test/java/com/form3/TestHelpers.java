package com.form3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.form3.domain.Party;
import com.form3.domain.Payment;
import com.form3.domain.PaymentAttributes;
import com.form3.domain.PaymentScheme;
import com.form3.domain.PaymentStatus;
import com.form3.domain.PaymentType;
import com.form3.domain.Type;
import com.form3.domain.builders.PartyBuilder;
import com.form3.domain.builders.PaymentAttributesBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Link;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class TestHelpers {

    public static Payment createPayment(BigDecimal amount, Currency currency) {
        Party originatingParty = new PartyBuilder()
                .setAccountNumber("41426819")
                .setAccountName("Emilia Brown")
                .setBankId("20-33-01")
                .setBankIdCode("GBDSC")
                .build();
        Party beneficiaryParty = new PartyBuilder()
                .setAccountNumber("31926819")
                .setAccountName("Wilfred Owens")
                .setBankId("40-30-00")
                .setBankIdCode("GBDSC")
                .build();
        PaymentAttributes attributes = new PaymentAttributesBuilder()
                .setStatus(PaymentStatus.SUBMITTED)
                .setAmount(amount)
                .setCurrency(currency)
                .setPaymentType(PaymentType.CREDIT)
                .setPaymentScheme(PaymentScheme.FPS)
                .setOriginatingParty(originatingParty)
                .setBeneficiaryParty(beneficiaryParty)
                .build();
        return new Payment(Type.PAYMENTS, attributes);
    }

    public static Payment createPayment() {
        return createPayment(new BigDecimal("100.21"), Currency.getInstance("GBP"));
    }

    public static Link getLink(Set<Link> links, String rel) {
        return links.stream().filter(link -> link.getRel().equals(rel)).collect(Collectors.toList()).get(0);
    }

    public static Invocation.Builder getInvocationBuilder(Client client, URI targetUri) {
        return client.target(targetUri).request().header("Authorization", "Basic cGVlc2tpbGxldDpwYXNz");
    }

    private static Client client = null;

    public static Client getJerseyClient() {
        if (client != null) {
            return client;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        return ClientBuilder.newClient().register(new JacksonJsonProvider(mapper));
    }
}
