package com.form3.db;

import com.form3.domain.Errors;
import com.form3.domain.Payment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

public class MockPaymentsDataService implements PaymentsDataService {

    private Map<String, Payment> datastore = new HashMap<>();

    public Optional<Payment> get(String id) {
        return Optional.ofNullable(datastore.get(id));
    }

    public String store(Payment payment) {
        String id = UUID.randomUUID().toString();
        datastore.put(id, payment.createWithId(id));
        return id;
    }

    public void delete(String id) {
        datastore.remove(id);
    }

    public Optional<Errors> update(String id, Payment payment) {
        Optional<Errors> errors = getErrors(id, payment);
        if (errors.isPresent()) {
            return errors;
        }
        datastore.put(id, payment);
        return Optional.empty();
    }

    public Collection<Payment> getAll() {
        return datastore.values();
    }

    private Optional<Errors> getErrors(String id, Payment update) {
        Optional<Payment> payment = get(id);

        if (!payment.isPresent()) {
            return Optional.of(new Errors(Errors.Code.PAYMENT_ID_NOT_FOUND, format("Payment with id %s does not exist", id)));
        }

        Errors e = null;
        if (payment.get().getType() != update.getType()) {
            e = new Errors(Errors.Code.CANNOT_UPDATE_IMMUTABLE_VALUES, "Cannot update immutable value payment type");
        }
        if (payment.get().getAttributes().getStatus() != update.getAttributes().getStatus()) {
            e = getErrors(e, "Updating payment status is not allowed");
        }
        if (!update.getId().isPresent()) {
            e = getErrors(e, "Updating the id is not allowed");
        }
        if (!id.equals(update.getId().get())) {
            e = getErrors(e, "Updating the id is not allowed");
        }
        return Optional.ofNullable(e);
    }

    private Errors getErrors(Errors e, String message) {
        if (e == null) {
            e = new Errors(Errors.Code.CANNOT_UPDATE_IMMUTABLE_VALUES, message);
        } else {
            e.addMessage(message);
        }
        return e;
    }

    public void clear() {
        datastore = new HashMap<>();
    }
}
