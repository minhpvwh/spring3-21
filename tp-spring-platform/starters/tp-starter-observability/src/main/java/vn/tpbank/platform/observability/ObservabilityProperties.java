package vn.tpbank.platform.observability;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tp.platform.observability")
public class ObservabilityProperties {

    private String tracingIdHeader = "X-Tracing-Id";
    private String transactionIdHeader = "X-Transaction-Id";
    private boolean logRequestBody = false;
    private boolean logResponseBody = false;

    public String getTracingIdHeader() {
        return tracingIdHeader;
    }

    public void setTracingIdHeader(String tracingIdHeader) {
        this.tracingIdHeader = tracingIdHeader;
    }

    public String getTransactionIdHeader() {
        return transactionIdHeader;
    }

    public void setTransactionIdHeader(String transactionIdHeader) {
        this.transactionIdHeader = transactionIdHeader;
    }

    public boolean isLogRequestBody() {
        return logRequestBody;
    }

    public void setLogRequestBody(boolean logRequestBody) {
        this.logRequestBody = logRequestBody;
    }

    public boolean isLogResponseBody() {
        return logResponseBody;
    }

    public void setLogResponseBody(boolean logResponseBody) {
        this.logResponseBody = logResponseBody;
    }
}
