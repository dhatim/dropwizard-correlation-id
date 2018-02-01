package org.dhatim.dropwizard.correlationid;

import java.util.Optional;
import java.util.UUID;
import org.apache.http.HttpRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

public final class CorrelationIdHttpClient {

    private CorrelationIdHttpClient() {
    }

    public static HttpClientBuilder wrap(HttpClientBuilder builder) {
        return wrap(builder, new CorrelationIdConfiguration());
    }

    public static HttpClientBuilder wrap(HttpClientBuilder builder, CorrelationIdConfiguration configuration) {
        builder.addInterceptorLast((HttpRequest request, HttpContext context) -> {
            String correlationId = Optional.ofNullable(MDC.get(configuration.mdcKey)).orElseGet(() -> UUID.randomUUID().toString());
            request.addHeader(configuration.headerName, correlationId);
        });
        return builder;
    }
}
