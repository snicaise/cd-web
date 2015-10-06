package com.polaris.cd.web.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.polaris.cd.web.api.ErrorMessage;
import com.polaris.cd.web.api.Quotation;
import com.polaris.cd.web.service.QuotationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 *
 */
@Path("booking")
@Produces(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final QuotationService quotationService;

    public BookingResource(QuotationService quotationService) {
        this.quotationService = checkNotNull(quotationService);
    }

    @GET
    @Path("/quotation")
    @Timed(name = "booking.quotation", absolute = true)
    public Response quotation(@QueryParam("origin") final String origin,
                               @QueryParam("destination") final String destination)
    {
        if (Strings.isNullOrEmpty(origin)) {
            return Response.status(BAD_REQUEST)
                .entity(new ErrorMessage("param 'origin' is mandatory"))
                .build();
        }
        if (Strings.isNullOrEmpty(destination)) {
            return Response.status(BAD_REQUEST)
                .entity(new ErrorMessage("param 'destination' is mandatory"))
                .build();
        }
        if (origin.equalsIgnoreCase(destination)) {
            return Response.status(BAD_REQUEST)
                .entity(new ErrorMessage("'origin' and 'destination' must be different"))
                .build();
        }

        Quotation quote = quotationService.quote(origin, destination);
        return Response.ok()
            .entity(quote)
            .build();
    }

}
