package vn.tpbank.platform.feign.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(StandardErrorDecoder.class);
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.warn("Feign client error: method={}, status={}, reason={}",
                methodKey, response.status(), response.reason());
        return defaultDecoder.decode(methodKey, response);
    }
}
