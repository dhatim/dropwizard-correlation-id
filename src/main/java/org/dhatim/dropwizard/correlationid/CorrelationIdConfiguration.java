package org.dhatim.dropwizard.correlationid;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public class CorrelationIdConfiguration {

    public final String headerName;
    public final String mdcKey;

    public CorrelationIdConfiguration() {
        this(null, null);
    }

    public CorrelationIdConfiguration(@JsonProperty("headerName") String headerName,
            @JsonProperty("mdcKey") String mdcKey) {
        this.headerName = Optional.ofNullable(headerName).orElse("X-Correlation-Id");
        this.mdcKey = Optional.ofNullable(mdcKey).orElse("correlationId");
    }

}
