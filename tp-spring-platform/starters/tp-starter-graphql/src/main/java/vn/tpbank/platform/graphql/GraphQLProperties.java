package vn.tpbank.platform.graphql;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for tp-starter-graphql.
 *
 * <pre>
 * tp:
 *   platform:
 *     graphql:
 *       introspection-enabled: false   # disable in prod
 *       max-query-depth: 10
 *       max-query-complexity: 100
 * </pre>
 */
@ConfigurationProperties(prefix = "tp.platform.graphql")
public class GraphQLProperties {

    /** Allow introspection queries. Should be false in production. */
    private boolean introspectionEnabled = true;

    /** Maximum allowed query depth. 0 = unlimited. */
    private int maxQueryDepth = 10;

    /** Maximum allowed query complexity. 0 = unlimited. */
    private int maxQueryComplexity = 100;

    public boolean isIntrospectionEnabled() {
        return introspectionEnabled;
    }

    public void setIntrospectionEnabled(boolean introspectionEnabled) {
        this.introspectionEnabled = introspectionEnabled;
    }

    public int getMaxQueryDepth() {
        return maxQueryDepth;
    }

    public void setMaxQueryDepth(int maxQueryDepth) {
        this.maxQueryDepth = maxQueryDepth;
    }

    public int getMaxQueryComplexity() {
        return maxQueryComplexity;
    }

    public void setMaxQueryComplexity(int maxQueryComplexity) {
        this.maxQueryComplexity = maxQueryComplexity;
    }
}
