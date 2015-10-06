package com.polaris.cd.web.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class DropwizardHealthCheck extends HealthCheck {

    private final WebTarget dropwizard;

    public DropwizardHealthCheck(Client client, String baseUri) {
        Preconditions.checkArgument(client != null);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(baseUri));

        this.dropwizard = client.target(baseUri);
    }

    @Override
    protected Result check() throws Exception {
        Response response = null;
        try {
            response = dropwizard.path("admin/ping").request(MediaType.TEXT_PLAIN).get();
            if (response.getStatus() != 200) {
                return Result.unhealthy("ping status code : %d", response.getStatus());
            }

            String content = response.readEntity(String.class);
            if (content != null && !content.startsWith("pong")) {
                return Result.unhealthy("ping response : %s", content);
            }
        } catch (Exception e) {
            return Result.unhealthy(e.getMessage());
        } finally {
             if (response != null) {
                 response.close();
             }
        }

        return Result.healthy();
    }

}
