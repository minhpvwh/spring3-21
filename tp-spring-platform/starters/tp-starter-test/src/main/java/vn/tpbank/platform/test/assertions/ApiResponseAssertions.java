package vn.tpbank.platform.test.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public final class ApiResponseAssertions {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ApiResponseAssertions() {}

    public static void assertSuccess(MvcResult result) throws Exception {
        JsonNode json = MAPPER.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("success").asBoolean()).isTrue();
        assertThat(json.get("code").asText()).isEqualTo("SUCCESS");
    }

    public static void assertError(MvcResult result, String expectedCode) throws Exception {
        JsonNode json = MAPPER.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("success").asBoolean()).isFalse();
        assertThat(json.get("code").asText()).isEqualTo(expectedCode);
    }
}
