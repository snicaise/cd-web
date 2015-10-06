package com.polaris.cd.web;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.polaris.cd.web.health.DropwizardHealthCheck;
import com.polaris.cd.web.resources.BookingResource;
import com.polaris.cd.web.service.BasicQuotationService;
import com.polaris.cd.web.service.PricingClient;
import com.polaris.cd.web.service.RecommendationClient;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;
import java.util.EnumSet;

/**
 *
 */
public class WebApplication extends Application<WebConfiguration> {

    public static void main(String[] args) throws Exception {
        new WebApplication().run(args);
    }

    @Override
    public String getName() {
        return "web";
    }

    @Override
    public void initialize(Bootstrap<WebConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );

        bootstrap.addBundle(new AssetsBundle("/dist", "/", "index.html"));
    }

    @Override
    public void run(WebConfiguration configuration, Environment environment) {
        environment.healthChecks().register("web", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });

        if (configuration.isCorsEnabled()) {
            FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
            filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,HEAD,OPTIONS");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MrBeanModule());

        final Client pricingJerseyClient = new JerseyClientBuilder(environment)
            .using(configuration.getPricingClientConfiguration())
            .using(mapper)
            .build("pricing");

        PricingClient pricingClient = new PricingClient(
            pricingJerseyClient,
            configuration.getPricingBaseUri());

        final Client recommendationJerseyClient = new JerseyClientBuilder(environment)
            .using(configuration.getRecommendationClientConfiguration())
            .using(mapper)
            .build("recommendation");

        RecommendationClient recommendationClient = new RecommendationClient(
            recommendationJerseyClient,
            configuration.getRecommendationBaseUri());

        environment.healthChecks().register("pricing",
            new DropwizardHealthCheck(
                pricingJerseyClient,
                configuration.getPricingBaseUri()));
        environment.healthChecks().register("recommendation",
            new DropwizardHealthCheck(
                recommendationJerseyClient,
                configuration.getRecommendationBaseUri()));

        BasicQuotationService quotationService = new BasicQuotationService(pricingClient,recommendationClient);
        BookingResource bookingResource = new BookingResource(quotationService);

        environment.jersey().register(bookingResource);
    }

}
