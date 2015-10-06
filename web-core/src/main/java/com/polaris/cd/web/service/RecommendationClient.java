package com.polaris.cd.web.service;

import com.polaris.cd.web.core.Recommendations;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 */
public class RecommendationClient {

    private final WebTarget pricing;

    public RecommendationClient(Client client, String baseUri) {
        this.pricing = client.target(baseUri);
    }

    public Recommendations recommended(String destination) {
        return pricing.path("recommendation/{destination}")
            .resolveTemplate("destination", destination)
            .request(MediaType.APPLICATION_JSON)
            .get(Recommendations.class);
    }

}
