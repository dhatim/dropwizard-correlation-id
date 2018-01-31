package org.dhatim.dropwizard.correlationid;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

public class CorrelationIdHttpClient implements HttpClient {

    private final HttpClient delegate;
    private final CorrelationIdConfiguration configuration;

    private CorrelationIdHttpClient(HttpClient delegate, CorrelationIdConfiguration configuration) {
        this.delegate = delegate;
        this.configuration = configuration;
    }

    public static HttpClient wrap(HttpClient delegate) {
        return wrap(delegate, new CorrelationIdConfiguration());
    }

    public static HttpClient wrap(HttpClient delegate, CorrelationIdConfiguration correlationIdConfiguration) {
        return new CorrelationIdHttpClient(delegate, correlationIdConfiguration);
    }

    private <H extends HttpRequest> H addHeader(H hr) {
        String correlationId = Optional.ofNullable(MDC.get(configuration.mdcKey)).orElseGet(() -> UUID.randomUUID().toString());
        hr.addHeader(configuration.headerName, correlationId);
        return hr;
    }

    @Override
    public HttpParams getParams() {
        return delegate.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return delegate.getConnectionManager();
    }

    @Override
    public HttpResponse execute(HttpUriRequest hur) throws IOException, ClientProtocolException {
        return delegate.execute(addHeader(hur));
    }

    @Override
    public HttpResponse execute(HttpUriRequest hur, HttpContext hc) throws IOException, ClientProtocolException {
        return delegate.execute(addHeader(hur), hc);
    }

    @Override
    public HttpResponse execute(HttpHost hh, HttpRequest hr) throws IOException, ClientProtocolException {
        return delegate.execute(hh, addHeader(hr));
    }

    @Override
    public HttpResponse execute(HttpHost hh, HttpRequest hr, HttpContext hc) throws IOException, ClientProtocolException {
        return delegate.execute(hh, addHeader(hr), hc);
    }

    @Override
    public <T> T execute(HttpUriRequest hur, ResponseHandler<? extends T> rh) throws IOException, ClientProtocolException {
        return delegate.execute(addHeader(hur), rh);
    }

    @Override
    public <T> T execute(HttpUriRequest hur, ResponseHandler<? extends T> rh, HttpContext hc) throws IOException, ClientProtocolException {
        return delegate.execute(addHeader(hur), rh, hc);
    }

    @Override
    public <T> T execute(HttpHost hh, HttpRequest hr, ResponseHandler<? extends T> rh) throws IOException, ClientProtocolException {
        return delegate.execute(hh, addHeader(hr), rh);
    }

    @Override
    public <T> T execute(HttpHost hh, HttpRequest hr, ResponseHandler<? extends T> rh, HttpContext hc) throws IOException, ClientProtocolException {
        return delegate.execute(hh, addHeader(hr), rh, hc);
    }

}
