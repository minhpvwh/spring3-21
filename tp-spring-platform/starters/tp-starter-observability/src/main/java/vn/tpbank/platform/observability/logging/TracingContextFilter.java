package vn.tpbank.platform.observability.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Extracts tracingId and transactionId from request headers, seeds MDC for every log line,
 * and echoes both values back in the response headers.
 * <p>
 * Header names are configurable via {@code tp.platform.observability.tracing-id-header}
 * and {@code tp.platform.observability.transaction-id-header}. Missing headers → generated UUID.
 */
public class TracingContextFilter extends OncePerRequestFilter {

    public static final String MDC_TRACING_ID = "tracingId";
    public static final String MDC_TRANSACTION_ID = "transactionId";

    private final String tracingIdHeader;
    private final String transactionIdHeader;

    public TracingContextFilter(String tracingIdHeader, String transactionIdHeader) {
        this.tracingIdHeader = tracingIdHeader;
        this.transactionIdHeader = transactionIdHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tracingId = header(request, tracingIdHeader);
        String transactionId = header(request, transactionIdHeader);

        putMdc(MDC_TRACING_ID, tracingId);
        putMdc(MDC_TRANSACTION_ID, transactionId);
        if (tracingId != null) response.setHeader(tracingIdHeader, tracingId);
        if (transactionId != null) response.setHeader(transactionIdHeader, transactionId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_TRACING_ID);
            MDC.remove(MDC_TRANSACTION_ID);
        }
    }

    private String header(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return (value != null && !value.isBlank()) ? value : null;
    }

    private void putMdc(String key, String value) {
        if (value != null) MDC.put(key, value);
    }
}
