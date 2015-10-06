package com.polaris.cd.web.resources;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.polaris.cd.ComponentTests;
import com.polaris.cd.web.WebApplication;
import com.polaris.cd.web.WebConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 *
 */
@Category(ComponentTests.class)
public class WebComponentTest {

    @ClassRule
    public static final DropwizardAppRule<WebConfiguration> RULE =
            new DropwizardAppRule<>(
                    WebApplication.class,
                    ResourceHelpers.resourceFilePath("test-server.yml")
            );

    @ClassRule
    public static WireMockClassRule pricingMock = new WireMockClassRule(8020);

    @ClassRule
    public static WireMockClassRule recommendationMock = new WireMockClassRule(8021);

    @Test
    public void quotation_validQuery_returnAmountAndRecommendations() {
        pricingMock.stubFor(get(urlEqualTo("/pricing/price?origin=paris&destination=london"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"origin\":\"paris\",\"destination\":\"london\",\"price\":254}")));

        recommendationMock.stubFor(get(urlEqualTo("/recommendation/london"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"destination\":\"london\",\"recommendations\":[\"Hotel A\",\"Hotel 1\"]}")));

        given()
            .port(RULE.getLocalPort())
            .queryParam("origin", "paris")
            .queryParam("destination", "london")
        .when()
            .get("/api/booking/quotation")
        .then()
            .log().all().and()
            .statusCode(200)
            .body("origin", equalTo("paris"))
            .body("destination", equalTo("london"))
            .body("price", equalTo(254))
            .body("recommendations.size()", equalTo(2))
            .body("recommendations[0]", equalTo("Hotel A"))
            .body("recommendations[1]", equalTo("Hotel 1"));
    }

}