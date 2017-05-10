package com.form3.db;

import com.form3.domain.Errors;
import com.form3.domain.Payment;

import java.util.Collection;
import java.util.Optional;

public interface PaymentsDataService {

    Optional<Payment> get(String id);

    String store(Payment payment);

    void delete(String id);

    Optional<Errors> update(String id, Payment payment);

    Collection<Payment> getAll();
}
