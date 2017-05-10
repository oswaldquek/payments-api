# payment-api

How to start the payment-api application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/type-1.0-SNAPSHOT.jar server config.yml`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Try it out!
---

There is a sample create.json in the `examples` folder. To create a payment do:

`curl -H "Content-Type: application/json" -X POST -d "@create.json" http://localhost:8080/payments/create -v`

Which will return a set of HATEOAS links.

To get a payment do for example:

`curl -H "Content-Type: application/json" -X GET http://localhost:8080/payments/<some-id>`

To update a payment edit the `create.json` and then do for example:

`curl -H "Content-Type: application/json" -X PUT -d "@create.json" http://localhost:8080/payments/update/<some-id>`