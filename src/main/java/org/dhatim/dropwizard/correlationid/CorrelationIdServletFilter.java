package org.dhatim.dropwizard.correlationid;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

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
