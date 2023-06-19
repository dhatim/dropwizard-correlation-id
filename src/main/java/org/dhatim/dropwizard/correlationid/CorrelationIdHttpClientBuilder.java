package org.dhatim.dropwizard.correlationid;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.core.setup.Environment;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

public class CorrelationIdHttpClientBuilder extends HttpClientBuilder {

    private final CorrelationIdConfiguration configuration;

    public CorrelationIdHttpClientBuilder(MetricRegistry metricRegistry) {
        this(metricRegistry, new CorrelationIdConfiguration());
    }

    public CorrelationIdHttpClientBuilder(MetricRegistry metricRegistry, CorrelationIdConfiguration configuration) {
        super(metricRegistry);
        this.configuration = configuration;
    }

    public CorrelationIdHttpClientBuilder(Environment environment) {
        this(environment, new CorrelationIdConfiguration());
    }

    public CorrelationIdHttpClientBuilder(Environment environment, CorrelationIdConfiguration configuration) {
        super(environment);
        this.configuration = configuration;
    }

    @Override
    protected org.apache.hc.client5.http.impl.classic.HttpClientBuilder customizeBuilder(org.apache.hc.client5.http.impl.classic.HttpClientBuilder builder) {
        builder.addRequestInterceptorLast((HttpRequest request, EntityDetails entity, HttpContext context) -> {
            String correlationId = Optional.ofNullable(MDC.get(configuration.mdcKey)).orElseGet(() -> UUID.randomUUID().toString());
            request.addHeader(configuration.headerName, correlationId);
        });
        return builder;
    }
}
