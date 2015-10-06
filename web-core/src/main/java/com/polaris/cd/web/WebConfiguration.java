package com.polaris.cd.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 */
public class WebConfiguration extends Configuration {

    @Valid
    @NotNull
    private boolean corsEnable = false;

    @Valid
    @NotNull
    private String pricingBaseUri;

    @Valid
    @NotNull
    private JerseyClientConfiguration pricingClient = new JerseyClientConfiguration();

    @Valid
    @NotNull
    private String recommendationBaseUri;

    @Valid
    @NotNull
    private JerseyClientConfiguration recommendationClient = new JerseyClientConfiguration();

    @JsonProperty("corsEnable")
    public boolean isCorsEnabled() {
        return corsEnable;
    }

    @JsonProperty("corsEnable")
    public void setCorsEnable(boolean enableCors) {
        this.corsEnable = enableCors;
    }

    @JsonProperty("pricingClient")
    public void setPricingClientConfiguration(JerseyClientConfiguration configuration) {
        this.pricingClient = configuration;
    }

    @JsonProperty("pricingClient")
    public JerseyClientConfiguration getPricingClientConfiguration() {
        return pricingClient;
    }

    @JsonProperty("recommendationClient")
    public void setRecommendationClientConfiguration(JerseyClientConfiguration configuration) {
        this.recommendationClient = configuration;
    }

    @JsonProperty("recommendationClient")
    public JerseyClientConfiguration getRecommendationClientConfiguration() {
        return recommendationClient;
    }

    @JsonProperty("pricingBaseUri")
    public String getPricingBaseUri() {
        return pricingBaseUri;
    }

    @JsonProperty("pricingBaseUri")
    public void setPricingBaseUri(String pricingBaseUri) {
        this.pricingBaseUri = pricingBaseUri;
    }

    @JsonProperty("recommendationBaseUri")
    public String getRecommendationBaseUri() {
        return recommendationBaseUri;
    }

    @JsonProperty("recommendationBaseUri")
    public void setRecommendationBaseUri(String recommendationBaseUri) {
        this.recommendationBaseUri = recommendationBaseUri;
    }
}
