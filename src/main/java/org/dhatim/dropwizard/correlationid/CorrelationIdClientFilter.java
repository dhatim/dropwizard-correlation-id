package org.dhatim.dropwizard.correlationid;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Provider
public class CorrelationIdClientFilter implements ClientRequestFilter {

    private final CorrelationIdConfiguration configuration;

    public CorrelationIdClientFilter() {
        this(new CorrelationIdConfiguration());
    }

    public CorrelationIdClientFilter(CorrelationIdConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String correlationId = Optional.ofNullable(MDC.get(configuration.mdcKey)).orElseGet(() -> UUID.randomUUID().toString());
        requestContext.getHeaders().add(configuration.headerName, correlationId);
    }

}
