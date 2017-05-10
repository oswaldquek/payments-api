package com.form3.helpers;

import javax.ws.rs.core.Link;

public class LinksHelper {

    public static Link[] getLinks(String id) {
        Link self = Link.fromUri("/payments/" + id).rel("self").build();
        Link delete = Link.fromUri("/payments/delete/" + id).rel("delete").build();
        Link update = Link.fromUri("/payments/update/" + id).rel("update").build();
        return new Link[]{self, delete, update};
    }
}
