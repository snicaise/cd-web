package com.polaris.cd.web.resources;

import com.polaris.cd.web.api.ErrorMessage;
import com.polaris.cd.web.api.Quotation;
import com.polaris.cd.web.service.QuotationService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 */
public class BookingResourceTest {

    private Quotation aQuotation;

    private static final QuotationService quotationService = mock(QuotationService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BookingResource(quotationService))
            .build();

    @Before
    public void setUp() {
        aQuotation = new Quotation();
        aQuotation.setOrigin("paris");
        aQuotation.setDestination("londres");
        aQuotation.setPrice(100);
        aQuotation.setRecommendations(Arrays.asList("Hotel 1", "Hotel 2"));

        reset(quotationService);
        when(quotationService.quote(anyString(), anyString())).thenReturn(aQuotation);
    }

    @Test
    public void quotation_aTrip_returnAQuote() throws Exception {
        Response response = resources.client()
            .target("/booking/quotation")
                .queryParam("origin", "paris")
                .queryParam("destination", "londres")
            .request().get();

        assertThat(response.getStatus()).isEqualTo(200);
        verify(quotationService, times(1)).quote("paris", "londres");

        Quotation quote = response.readEntity(Quotation.class);
        assertThat(quote.getPrice()).isEqualTo(100);
        assertThat(quote.getRecommendations()).containsExactly("Hotel 1", "Hotel 2");
        assertThat(quote.getOrigin()).isEqualTo("paris");
        assertThat(quote.getDestination()).isEqualTo("londres");
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void quotation_noOrigin_returnError() throws Exception {
        Response response = resources.client()
            .target("/booking/quotation")
                .queryParam("destination", "londres")
            .request().get();

        assertThat(response.getStatus()).isEqualTo(400);
        verify(quotationService, never()).quote(anyString(), anyString());

        ErrorMessage error = response.readEntity(ErrorMessage.class);
        assertThat(error.getMessage()).contains("origin", "mandatory");
    }

    @Test
    public void quotation_noDestination_returnError() throws Exception {
        Response response = resources.client()
            .target("/booking/quotation")
                .queryParam("origin", "paris")
            .request().get();

        assertThat(response.getStatus()).isEqualTo(400);
        verify(quotationService, never()).quote(anyString(), anyString());

        ErrorMessage error = response.readEntity(ErrorMessage.class);
        assertThat(error.getMessage()).contains("destination", "mandatory");
    }

    @Test
    public void quotation_sameOriginDestination_returnError() throws Exception {
        Response response = resources.client()
            .target("/booking/quotation")
                .queryParam("origin", "paris")
                .queryParam("destination", "paris")
            .request().get();

        assertThat(response.getStatus()).isEqualTo(400);
        verify(quotationService, never()).quote(anyString(), anyString());

        ErrorMessage error = response.readEntity(ErrorMessage.class);
        assertThat(error.getMessage()).contains("destination", "origin");
    }

}