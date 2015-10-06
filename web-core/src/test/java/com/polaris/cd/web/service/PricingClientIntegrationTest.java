package com.polaris.cd.web.service;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.polaris.cd.IntegrationTests;
import com.polaris.cd.web.core.Pricing;
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
public class PricingClientIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(0);

    private PricingClient client;

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

        client = new PricingClient(jerseyClient, "http://localhost:"+wireMockRule.port());
    }

    @Test
    public void price_validResponse_returnPrice() {
        wireMockRule.stubFor(get(urlEqualTo("/pricing/price?origin=paris&destination=londres"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"origin\":\"paris\",\"destination\":\"londres\",\"price\":254}")));

        Pricing price = client.price("paris", "londres");

        assertThat(price.getOrigin()).isEqualTo("paris");
        assertThat(price.getDestination()).isEqualTo("londres");
        assertThat(price.getPrice()).isEqualTo(254);
    }

    @Test
    public void price_http500_throwException() {
        // given
        wireMockRule.stubFor(get(urlEqualTo("/pricing/price?origin=paris&destination=londres"))
            .willReturn(aResponse()
                .withStatus(500)));

        // when
        Throwable thrown = catchThrowable(() -> client.price("paris", "londres"));

        // then
        assertThat(thrown)
            .isInstanceOf(ServerErrorException.class)
            .hasMessageContaining("Internal Server Error");
    }

    @Test
    public void price_timeout_throwException() {
        // given
        wireMockRule.stubFor(get(urlEqualTo("/pricing/price?origin=paris&destination=londres"))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(2000)));

        // when
        Throwable thrown = catchThrowable(() -> client.price("paris", "londres"));

        // then
        assertThat(thrown)
            .isInstanceOf(ProcessingException.class)
            .hasMessageContaining("Read timed out");
    }
}