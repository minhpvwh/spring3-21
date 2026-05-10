package vn.tpbank.platform.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

public class TraceIdRequestInterceptor implements RequestInterceptor {

    private final String tracingIdHeader;
    private final String transactionIdHeader;

    public TraceIdRequestInterceptor(String tracingIdHeader, String transactionIdHeader) {
        this.tracingIdHeader = tracingIdHeader;
        this.transactionIdHeader = transactionIdHeader;
    }

    @Override
    public void apply(RequestTemplate template) {
        propagate(template, "tracingId", tracingIdHeader);
        propagate(template, "transactionId", transactionIdHeader);
        propagate(template, "traceId", "X-Trace-ID");
    }

    private void propagate(RequestTemplate template, String mdcKey, String headerName) {
        String value = MDC.get(mdcKey);
        if (value != null) {
            template.header(headerName, value);
        }
    }
}
