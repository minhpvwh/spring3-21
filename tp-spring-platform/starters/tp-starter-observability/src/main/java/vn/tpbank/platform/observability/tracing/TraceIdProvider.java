package vn.tpbank.platform.observability.tracing;

import io.micrometer.tracing.Tracer;
import org.springframework.lang.Nullable;

public class TraceIdProvider {

    private final Tracer tracer;

    public TraceIdProvider(Tracer tracer) {
        this.tracer = tracer;
    }

    @Nullable
    public String getCurrentTraceId() {
        var span = tracer.currentSpan();
        if (span == null) {
            return null;
        }
        return span.context().traceId();
    }

    @Nullable
    public String getCurrentSpanId() {
        var span = tracer.currentSpan();
        if (span == null) {
            return null;
        }
        return span.context().spanId();
    }
}
