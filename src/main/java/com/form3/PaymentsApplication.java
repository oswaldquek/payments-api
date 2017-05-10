package com.form3;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.form3.api.CreatePayments;
import com.form3.api.DeletePayments;
import com.form3.api.GetPayments;
import com.form3.api.UpdatePayments;
import com.form3.db.PaymentsDataService;
import com.form3.db.PaymentsDataServiceImpl;
import com.form3.healthcheck.PaymentsApiHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class PaymentsApplication extends Application<PaymentsApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new PaymentsApplication().run(args);
    }

    @Override
    public void run(PaymentsApplicationConfiguration configuration, Environment environment) throws Exception {
        PaymentsDataService db = new PaymentsDataServiceImpl();
        environment.jersey().register(new CreatePayments(db));
        environment.jersey().register(new GetPayments(db));
        environment.jersey().register(new UpdatePayments(db));
        environment.jersey().register(new DeletePayments(db));

        environment.healthChecks().register("payments-api", new PaymentsApiHealthCheck());
    }
}
