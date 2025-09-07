package com.petstore.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // Log Request
        logger.info("===== REQUEST =====");
        logger.info("Method: {} {}", requestSpec.getMethod(), requestSpec.getURI());
        logger.info("Headers: {}", requestSpec.getHeaders());

        if (requestSpec.getBody() != null) {
            logger.info("Body: {}", (Object) requestSpec.getBody());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        // Log Response
        logger.info("===== RESPONSE =====");
        logger.info("Status Code: {}", response.getStatusCode());
        logger.info("Headers: {}", response.getHeaders());
        logger.info("Body: {}", response.getBody().asString());
        logger.info("Response Time: {} ms", response.getTime());

        return response;
    }
}
