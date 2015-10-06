package com.polaris.cd.web.service;

import com.polaris.cd.web.api.Quotation;

/**
 *
 */
public interface QuotationService {

    Quotation quote(String origin, String destination);

}
