package org.dhatim.dropwizard.correlationid;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import java.util.Optional;
import java.util.UUID;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

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
    protected org.apache.http.impl.client.HttpClientBuilder customizeBuilder(org.apache.http.impl.client.HttpClientBuilder builder) {
        builder.addInterceptorLast((HttpRequest request, HttpContext context) -> {
            String correlationId = Optional.ofNullable(MDC.get(configuration.mdcKey)).orElseGet(() -> UUID.randomUUID().toString());
            request.addHeader(configuration.headerName, correlationId);
        });
        return builder;
    }

}
