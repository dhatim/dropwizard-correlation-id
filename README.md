# Dropwizard Correlation Id

[![Build Status](https://github.com/dhatim/dropwizard-correlation-id/workflows/build/badge.svg)](https://github.com/dhatim/dropwizard-correlation-id/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-jwt-cookie-authentication/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dhatim/dropwizard-correlation)id
[![Javadocs](https://www.javadoc.io/badge/org.dhatim/dropwizard-correlation-id.svg)](https://www.javadoc.io/doc/org.dhatim/dropwizard-correlation-id)

Correlation ids for Dropwizard applications. They are useful to match requests between different components.
- Correlation ids are sent from one system to another using an http header in requests and responses. The default http header is `X-Correlation-Id`.
- When the server processes a request, its correlation id (or a random UUID if not available) is put into [slf4j mapped diagnostic context (MDC)](https://www.slf4j.org/manual.html#mdc). The default MDC key is `correlationId`.
- When using a Jersey or Apache http client to send requests to another system, the correlation id currently in the MDC (or a random UUID if not available) is put into the request http header.

## Usage

### Maven Artifacts

This project is available in the [Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.dhatim%22%20AND%20a%3A%22dropwizard-correlation-id%22). To add it to your project simply add the following dependency to your POM:

```xml
<dependency>
  <groupId>org.dhatim</groupId>
  <artifactId>dropwizard-correlation-id</artifactId>
  <version>0.1.0</version>
</dependency>
```

### Optional: edit your configuration YAML

The default values are as follows:
```yaml
correlationId:
    headerName: X-Correlation-Id
    mdcKey: correlationId
```

### Add the bundle to your Dropwizard application

Without configuration:
```java
public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap) {
    bootstrap.addBundle(CorrelationIdBundle.getDefault());
}
```
With configuration:
```java
public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap) {
    bootstrap.addBundle(CorrelationIdBundle.withConfigurationSupplier(MyAppConfiguration::getCorrelationId));
}
```

### Usage with a Jersey client

Just register the provided filter into your Jersey client:
```java
JerseyClientBuilder builder = new JerseyClientBuilder(...)...;
Client client = builder.build(...);
client.register(new CorrelationIdClientFilter(configuration));
```
If `configuration` is omitted, default values apply.

### Usage with an Apache http client

Replace your `HttpClientBuilder` by `CorrelationIdHttpClientBuilder` this way:
```java
HttpClientBuilder builder = new CorrelationIdHttpClientBuilder(..., configuration)...;
CloseableHttpClient cient = builder.build(...);
```
If `configuration` is omitted, default values apply.

## Support

Please file bug reports and feature requests in [GitHub issues](https://github.com/dhatim/dropwizard-correlation-id/issues).
