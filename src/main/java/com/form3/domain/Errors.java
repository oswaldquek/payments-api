package com.form3.domain;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class Errors {

    private Code code;
    private List<String> messages = new ArrayList<>();

    public Errors(Code code, String message) {
        this.code = code;
        messages.add(message);
    }

    public Code getCode() {
        return code;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public enum Code {
        PAYMENT_ID_NOT_FOUND(Response.Status.NOT_FOUND),
        CANNOT_UPDATE_IMMUTABLE_VALUES(Response.Status.BAD_REQUEST);

        public final Response.Status status;

        Code(Response.Status status) {
            this.status = status;
        }
    }
}
