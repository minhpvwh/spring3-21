package vn.tpbank.platform.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.Nullable;
import vn.tpbank.platform.web.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates exceptions thrown by GraphQL resolvers into {@link GraphQLError} using
 * the same error codes as the REST {@code GlobalExceptionHandler}.
 *
 * <p>Extensions map always contains {@code code}, {@code tracingId}, {@code transactionId}
 * for consistency with the REST {@code ApiResponse} envelope.
 */
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @Override
    @Nullable
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof BusinessException be) {
            log.warn("GraphQL business exception: code={}, message={}", be.getErrorCode().getCode(), ex.getMessage());
            return GraphqlErrorBuilder.newError(env)
                    .errorType(toErrorType(be))
                    .message(ex.getMessage())
                    .extensions(extensions(be.getErrorCode().getCode()))
                    .build();
        }

        log.error("GraphQL unhandled exception in field '{}'", env.getField().getName(), ex);
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("An unexpected error occurred")
                .extensions(extensions("ERR_INTERNAL"))
                .build();
    }

    private ErrorType toErrorType(BusinessException be) {
        return switch (be.getErrorCode()) {
            case NOT_FOUND -> ErrorType.NOT_FOUND;
            case UNAUTHORIZED -> ErrorType.UNAUTHORIZED;
            case FORBIDDEN -> ErrorType.FORBIDDEN;
            case BAD_REQUEST, VALIDATION_FAILED -> ErrorType.BAD_REQUEST;
            default -> ErrorType.INTERNAL_ERROR;
        };
    }

    private Map<String, Object> extensions(String code) {
        Map<String, Object> ext = new HashMap<>();
        ext.put("code", code);
        ext.put("tracingId", MDC.get("tracingId"));
        ext.put("transactionId", MDC.get("transactionId"));
        return ext;
    }
}
