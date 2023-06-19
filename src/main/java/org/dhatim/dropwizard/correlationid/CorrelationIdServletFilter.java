package org.dhatim.dropwizard.correlationid;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class CorrelationIdServletFilter implements Filter {

    private final CorrelationIdConfiguration configuration;

    public CorrelationIdServletFilter(CorrelationIdConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String correlationId = Optional.ofNullable(req.getHeader(configuration.headerName)).orElseGet(() -> UUID.randomUUID().toString());
        MDC.put(configuration.mdcKey, correlationId);
        resp.addHeader(configuration.headerName, correlationId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
