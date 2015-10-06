package com.polaris.cd.web.service;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.polaris.cd.IntegrationTests;
import com.polaris.cd.web.core.Recommendations;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import java.util.concurrent.Executors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 *
 */
@Category(IntegrationTests.class)
public class RecommendationClientIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    private RecommendationClient client;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MrBeanModule());

        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setConnectionRequestTimeout(Duration.milliseconds(500));
        jerseyClientConfiguration.setConnectionTimeout(Duration.milliseconds(500));

        final Client jerseyClient = new JerseyClientBuilder(new MetricRegistry())
            .using(jerseyClientConfiguration)
            .using(mapper)
            .using(Executors.newFixedThreadPool(5))
            .build("pricing");

        client = new RecommendationClient(jerseyClient, "http://localhost:"+wireMockRule.port());
    }

    @Test
    public void recommendation_validResponse_returnPrice() {
        wireMockRule.stubFor(get(urlEqualTo("/recommendation/paris"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"destination\":\"paris\", \"recommendations\":[\"Hotel A\",\"Hotel 1\"]}")));

        Recommendations recommendations = client.recommended("paris");

        assertThat(recommendations.getDestination()).isEqualTo("paris");
        assertThat(recommendations.getRecommendations()).containsExactly("Hotel A", "Hotel 1");
    }

    @Test
    public void recommendation_http500_throwException() {
        // given
        wireMockRule.stubFor(get(urlEqualTo("/recommendation/paris"))
            .willReturn(aResponse()
                .withStatus(500)));

        // when
        Throwable thrown = catchThrowable(() -> client.recommended("paris"));

        // then
        assertThat(thrown)
            .isInstanceOf(ServerErrorException.class)
            .hasMessageContaining("Internal Server Error");
    }

    @Test
    public void recommendation_timeout_throwException() {
        // given
        wireMockRule.stubFor(get(urlEqualTo("/recommendation/paris"))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(2000)));

        // when
        Throwable thrown = catchThrowable(() -> client.recommended("paris"));

        // then
        assertThat(thrown)
            .isInstanceOf(ProcessingException.class)
            .hasMessageContaining("Read timed out");
    }
}