package com.hello.suripu.admin.resources.v1;

import com.google.common.base.Optional;
import com.hello.suripu.admin.Util;
import com.hello.suripu.admin.diagnostic.Count;
import com.hello.suripu.admin.diagnostic.DiagnosticDAO;
import com.hello.suripu.core.db.AccountDAO;
import com.hello.suripu.core.db.DeviceDAO;
import com.hello.suripu.core.models.DeviceAccountPair;
import com.hello.suripu.core.oauth.AccessToken;
import com.hello.suripu.core.oauth.OAuthScope;
import com.hello.suripu.core.oauth.Scope;
import com.hello.suripu.core.util.JsonError;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1/diagnostic")
public class DiagnosticResources {

    private final DiagnosticDAO diagnosticDAO;
    private final AccountDAO accountDAO;
    private final DeviceDAO deviceDAO;


    public DiagnosticResources(final DiagnosticDAO diagnosticDAO, final AccountDAO accountDAO, final DeviceDAO deviceDAO) {
        this.diagnosticDAO = diagnosticDAO;
        this.accountDAO = accountDAO;
        this.deviceDAO = deviceDAO;
    }

    @GET
    @Path("/uptime/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Count> uptime(@Scope(OAuthScope.ADMINISTRATION_READ) final AccessToken accessToken,
                              @PathParam("email") final String email) {

        final Optional<Long> accountIdOptional = Util.getAccountIdByEmail(accountDAO, email);
        if(!accountIdOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(
                    new JsonError(Response.Status.NOT_FOUND.getStatusCode(), "Account not found")).build());
        }

        final Optional<DeviceAccountPair> deviceAccountPairOptional = deviceDAO.getMostRecentSensePairByAccountId(accountIdOptional.get());
        if(!deviceAccountPairOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(
                    new JsonError(Response.Status.NOT_FOUND.getStatusCode(), "Device not found")).build());
        }

        final List<Count> counts = diagnosticDAO.uptime(accountIdOptional.get(), deviceAccountPairOptional.get().internalDeviceId);
        return counts;
    }
}