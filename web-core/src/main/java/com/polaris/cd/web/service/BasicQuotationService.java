package com.polaris.cd.web.service;

import com.polaris.cd.web.api.Quotation;
import com.polaris.cd.web.core.Pricing;
import com.polaris.cd.web.core.Recommendations;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class BasicQuotationService implements QuotationService {

    private final PricingClient pricingClient;
    private final RecommendationClient recommendationClient;

    public BasicQuotationService(PricingClient pricingClient, RecommendationClient recommendationClient) {
        this.pricingClient = checkNotNull(pricingClient);
        this.recommendationClient = checkNotNull(recommendationClient);
    }

    @Override
    public Quotation quote(String origin, String destination) {
        Pricing pricing = pricingClient.price(origin, destination);
        Recommendations recommendations = recommendationClient.recommended(destination);

        Quotation quotation = new Quotation();
        quotation.setOrigin(origin);
        quotation.setDestination(destination);
        quotation.setPrice(pricing.getPrice());
        quotation.setRecommendations(recommendations.getRecommendations());

        return quotation;
    }

}
