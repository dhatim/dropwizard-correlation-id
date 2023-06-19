package org.dhatim.dropwizard.correlationid;

import io.dropwizard.core.Configuration;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.servlet.DispatcherType;

import java.util.EnumSet;
import java.util.function.Function;

public class CorrelationIdBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private final Function<C, CorrelationIdConfiguration> configurationSupplier;

    public CorrelationIdBundle(Function<C, CorrelationIdConfiguration> configurationSupplier) {
        this.configurationSupplier = configurationSupplier;
    }

    public static <C extends Configuration> CorrelationIdBundle<C> withConfigurationSupplier(Function<C, CorrelationIdConfiguration> configurationSupplier) {
        return new CorrelationIdBundle<>(configurationSupplier);
    }

    public static <C extends Configuration> CorrelationIdBundle<C> getDefault() {
        return withConfigurationSupplier(c -> new CorrelationIdConfiguration());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        CorrelationIdConfiguration correlationIdConfiguration = configurationSupplier.apply(configuration);
        environment.servlets()
                .addFilter("correlation-id-servlet-filter", new CorrelationIdServletFilter(correlationIdConfiguration))
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");
    }

}
