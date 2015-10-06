package com.polaris.cd.web.service;

import com.polaris.cd.web.api.Quotation;
import com.polaris.cd.web.core.Pricing;
import com.polaris.cd.web.core.Recommendations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BasicQuotationServiceTest {

    @Mock
    private PricingClient pricingClient;

    @Mock
    private RecommendationClient recommendationClient;

    @InjectMocks
    private BasicQuotationService quotationService;

    @Test
    public void quote_returnQuotation () {
        // given
        Pricing aPrice = new Pricing("paris", "londres", 200);
        when(pricingClient.price(anyString(), anyString())).thenReturn(aPrice);

        Recommendations aRecommendation = new Recommendations("londres", Arrays.asList("R1", "R2"));
        when(recommendationClient.recommended(anyString())).thenReturn(aRecommendation);

        // when
        Quotation quote = quotationService.quote("paris", "londres");

        // then
        verify(pricingClient, times(1)).price("paris", "londres");
        verify(recommendationClient, times(1)).recommended("londres");

        assertThat(quote.getOrigin()).isEqualTo("paris");
        assertThat(quote.getDestination()).isEqualTo("londres");
        assertThat(quote.getPrice()).isEqualTo(200);
        assertThat(quote.getRecommendations()).containsExactly("R1", "R2");
    }

    @Test
    public void quote_pricingThrow_reThrowError () {
        // given
        when(pricingClient.price(anyString(), anyString())).thenThrow(new RuntimeException("oups"));

        // when
        Throwable thrown = catchThrowable(() -> quotationService.quote("paris", "londres"));

        // then
        assertThat(thrown)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("oups");
        verify(recommendationClient, never()).recommended(anyString());
    }

    @Test
    public void quote_recommendationThrow_reThrowError () {
        // given
        when(recommendationClient.recommended(anyString())).thenThrow(new RuntimeException("oups"));

        // when
        Throwable thrown = catchThrowable(() -> quotationService.quote("paris", "londres"));

        // then
        assertThat(thrown)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("oups");
        verify(pricingClient, times(1)).price("paris", "londres");
    }

}