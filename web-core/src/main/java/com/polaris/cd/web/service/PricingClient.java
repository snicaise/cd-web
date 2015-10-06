package com.polaris.cd.web.service;


import com.polaris.cd.web.core.Pricing;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 */
public class PricingClient {

    private final WebTarget pricing;

    public PricingClient(Client client, String baseUri) {
        this.pricing = client.target(baseUri);
    }

    public Pricing price(String origin, String destination) {
        return pricing.path("pricing/price")
            .queryParam("origin", origin)
            .queryParam("destination", destination)
            .request(MediaType.APPLICATION_JSON)
            .get(Pricing.class);
    }

}
