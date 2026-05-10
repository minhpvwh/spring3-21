package vn.tpbank.platform.graphql.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import vn.tpbank.platform.graphql.GraphQLProperties;
import vn.tpbank.platform.graphql.exception.GraphQLExceptionHandler;
import vn.tpbank.platform.graphql.interceptor.TracingGraphQLInterceptor;
import vn.tpbank.platform.observability.ObservabilityProperties;

@AutoConfiguration
@ConditionalOnClass(WebGraphQlInterceptor.class)
@EnableConfigurationProperties(GraphQLProperties.class)
public class GraphQLAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DataFetcherExceptionResolver.class)
    public GraphQLExceptionHandler graphQLExceptionHandler() {
        return new GraphQLExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(WebGraphQlInterceptor.class)
    public TracingGraphQLInterceptor tracingGraphQLInterceptor(ObservabilityProperties observabilityProperties) {
        return new TracingGraphQLInterceptor(
                observabilityProperties.getTracingIdHeader(),
                observabilityProperties.getTransactionIdHeader()
        );
    }
}
