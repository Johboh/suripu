package com.hello.suripu.factory.resources;

import com.hello.suripu.core.db.KeyStore;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("register")
public class FactoryResource {

    private final KeyStore keyStore;

    public FactoryResource(final KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @PUT
    @Timed
    @Path("/{device_id}")
    public Response addPublicKeyForDeviceId(
            @PathParam("device_id") final String deviceId,
            final String body) {

        if(body.isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Can not have empty body")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        // Body = base64 encoded public key
        // TODO : validate device id format

        keyStore.put(deviceId, body);
        return Response.ok().build();
    }
}
