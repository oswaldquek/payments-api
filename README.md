# payment-api

Requirements
---

1. [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
1. [maven](https://maven.apache.org/install.html)

How to start the payment-api application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/type-1.0-SNAPSHOT.jar server config.yml`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Try it out!
---

This application uses JWT for authorization. In order to perform CRUD payment operations you need to get a JWT token first. To get a token do:

`curl -H "Content-Type: application/json" -X POST -d '{"user":"Joe Bloggs","password":"p4ssw0rd"}' http://localhost:8080/payments/login -v`

This will return a token which is needed for all calls to the payments api. You send the token by setting the Authorization header in the request. See below for examples. You can set the expiry of the token in the config.yml.

Note: The user/password of "Joe Bloggs"/"p4ssw0rd" is currently hard coded in the MockUserValidationService.

There is a sample create.json in the `examples` folder. To create a payment do:

`curl -H "Content-Type: application/json" -X POST -d "@create.json" -H "Authorization: Bearer <token>" http://localhost:8080/payments/create -v`

Which will return a set of HATEOAS links.

To get a payment do for example:

`curl -H "Content-Type: application/json" -X GET -H "Authorization: Bearer <token>" http://localhost:8080/payments/<some-id>`

To update a payment edit the `create.json` and then do for example:

`curl -H "Content-Type: application/json" -X PUT -d "@create.json" -H "Authorization: Bearer <token>" http://localhost:8080/payments/update/<some-id>`