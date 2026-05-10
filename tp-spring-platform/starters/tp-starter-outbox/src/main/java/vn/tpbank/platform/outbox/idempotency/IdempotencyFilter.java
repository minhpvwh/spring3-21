package vn.tpbank.platform.outbox.idempotency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;

public class IdempotencyFilter extends OncePerRequestFilter {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";

    private final IdempotencyKeyRepository repository;

    public IdempotencyFilter(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<IdempotencyKey> existing = repository.findById(idempotencyKey);
        if (existing.isPresent()) {
            IdempotencyKey cached = existing.get();
            response.setStatus(cached.getResponseStatus());
            response.setContentType("application/json");
            response.getWriter().write(cached.getResponseBody());
            return;
        }

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, wrappedResponse);

        IdempotencyKey key = new IdempotencyKey();
        key.setKey(idempotencyKey);
        key.setResponseStatus(wrappedResponse.getStatus());
        key.setResponseBody(new String(wrappedResponse.getContentAsByteArray()));
        repository.save(key);

        wrappedResponse.copyBodyToResponse();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }
}
