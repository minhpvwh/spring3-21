package vn.tpbank.platform.graphql.interceptor;

import org.slf4j.MDC;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

/**
 * Propagates {@code tracingId} and {@code transactionId} from HTTP request headers
 * into MDC and GraphQL context for every GraphQL execution.
 *
 * <p>For servlet-based apps, {@code TracingContextFilter} already seeds the MDC.
 * This interceptor additionally exposes the values via {@code graphql.GraphQLContext}
 * so resolvers can access them via {@code DataFetchingEnvironment.getGraphQlContext()}.
 */
public class TracingGraphQLInterceptor implements WebGraphQlInterceptor {

    private final String tracingIdHeader;
    private final String transactionIdHeader;

    public TracingGraphQLInterceptor(String tracingIdHeader, String transactionIdHeader) {
        this.tracingIdHeader = tracingIdHeader;
        this.transactionIdHeader = transactionIdHeader;
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String tracingId = request.getHeaders().getFirst(tracingIdHeader);
        String transactionId = request.getHeaders().getFirst(transactionIdHeader);

        // Expose in GraphQL context — accessible via DataFetchingEnvironment.getGraphQlContext()
        request.configureExecutionInput((input, builder) ->
                builder.graphQLContext(ctx -> {
                    if (tracingId != null) ctx.put("tracingId", tracingId);
                    if (transactionId != null) ctx.put("transactionId", transactionId);
                }).build());

        // Seed MDC for reactive pipelines (servlet path relies on TracingContextFilter)
        if (tracingId != null) MDC.put("tracingId", tracingId);
        if (transactionId != null) MDC.put("transactionId", transactionId);

        return chain.next(request)
                .doFinally(signal -> {
                    MDC.remove("tracingId");
                    MDC.remove("transactionId");
                });
    }
}
