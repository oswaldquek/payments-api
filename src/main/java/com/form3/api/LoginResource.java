package com.form3.api;

import com.form3.auth.SecretsManagementService;
import com.form3.auth.UserValidationService;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

@Path("/payments/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {
    private final SecretsManagementService secretsManagementService;
    private final UserValidationService userValidationService;
    private int jwtTokenValidity;

    public LoginResource(SecretsManagementService secretsManagementService, UserValidationService userValidationService, int jwtTokenValidity) {
        this.secretsManagementService = secretsManagementService;
        this.userValidationService = userValidationService;
        this.jwtTokenValidity = jwtTokenValidity;
    }

    @POST
    public Response generateValidToken(Map<String, String> params) {
        boolean successful = userValidationService.login(params.get("user"), params.get("password"));

        if (successful) {
            final JwtClaims claims = new JwtClaims();
            claims.setSubject("good-guy");
            NumericDate expirationTime = NumericDate.now();
            expirationTime.addSeconds(jwtTokenValidity);
            claims.setExpirationTime(expirationTime);

            final JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(HMAC_SHA256);

            try {
                jws.setKey(new HmacKey(secretsManagementService.getJwtTokenSecret()));
                return Response.ok(singletonMap("token", jws.getCompactSerialization())).build();
            } catch (JoseException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
