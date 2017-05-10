package com.form3;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class PaymentsApplication extends Application<PaymentsApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new PaymentsApplication().run(args);
    }

    @Override
    public void run(PaymentsApplicationConfiguration configuration, Environment environment) throws Exception {

    }
}
