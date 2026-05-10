package vn.tpbank.service.sample.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.tpbank.platform.web.exception.BusinessException;
import vn.tpbank.platform.web.exception.ErrorCode;
import vn.tpbank.platform.web.response.ApiResponse;
import vn.tpbank.platform.web.response.PageResponse;
import vn.tpbank.service.sample.model.dto.CreateSampleRequest;
import vn.tpbank.service.sample.model.dto.SampleResponse;

import java.time.Instant;
import java.util.List;

/**
 * Sample controller demonstrating all ApiResponse success/error patterns.
 *
 * SUCCESS cases:
 *   GET  /api/v1/samples/{id}    -> ApiResponse<SampleResponse>     (single object)
 *   GET  /api/v1/samples         -> ApiResponse<PageResponse>       (paginated list)
 *   POST /api/v1/samples         -> ApiResponse<SampleResponse>     (created object)
 *   PUT  /api/v1/samples/{id}    -> ApiResponse<SampleResponse>     (updated object)
 *   DELETE /api/v1/samples/{id}  -> ApiResponse<Void>               (no content)
 *
 * ERROR cases (triggered by specific IDs/inputs):
 *   id=0       -> ERR_BAD_REQUEST       (400)
 *   id=404     -> ERR_NOT_FOUND         (404)
 *   id=401     -> ERR_UNAUTHORIZED      (401)
 *   id=403     -> ERR_FORBIDDEN         (403)
 *   id=409     -> ERR_CONFLICT          (409)
 *   id=503     -> ERR_SERVICE_UNAVAILABLE (503)
 *   id=500     -> ERR_INTERNAL          (500) - unhandled exception
 *   POST with invalid body         -> ERR_VALIDATION (400)
 *   POST with malformed JSON       -> ERR_BAD_REQUEST (400)
 *   DELETE /api/v1/samples/{id}    -> ERR_METHOD_NOT_ALLOWED (405) when using wrong HTTP method
 *   GET with missing required param -> ERR_BAD_REQUEST (400)
 */
@RestController
@RequestMapping("/api/v1/samples")
public class SampleController {

    // ==================== SUCCESS CASES ====================

    @GetMapping("/{id}")
    public ApiResponse<SampleResponse> getById(@PathVariable Long id) {
        simulateErrorByIdIfNeeded(id);

        SampleResponse sample = new SampleResponse(id, "Sample-" + id, 100_000L, "Sample item", Instant.now());
        return ApiResponse.success(sample);
    }

    @GetMapping
    public ApiResponse<PageResponse<SampleResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<SampleResponse> items = List.of(
                new SampleResponse(1L, "Account A", 500_000L, "Savings account", Instant.now()),
                new SampleResponse(2L, "Account B", 1_200_000L, "Checking account", Instant.now()),
                new SampleResponse(3L, "Account C", 300_000L, "Investment account", Instant.now())
        );

        PageResponse<SampleResponse> pageResponse = new PageResponse<>(items, page, size, 25, 3);
        return ApiResponse.success(pageResponse);
    }

    @PostMapping
    public ApiResponse<SampleResponse> create(@Valid @RequestBody CreateSampleRequest request) {
        SampleResponse created = new SampleResponse(
                99L, request.getName(), request.getAmount(), request.getDescription(), Instant.now());
        return ApiResponse.success(created, "Sample created successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<SampleResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody CreateSampleRequest request) {
        simulateErrorByIdIfNeeded(id);

        SampleResponse updated = new SampleResponse(
                id, request.getName(), request.getAmount(), request.getDescription(), Instant.now());
        return ApiResponse.success(updated, "Sample updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        simulateErrorByIdIfNeeded(id);
        return ApiResponse.success(null, "Sample deleted successfully");
    }

    // ==================== DEMO: REQUIRED PARAM ====================

    @GetMapping("/search")
    public ApiResponse<List<SampleResponse>> search(@RequestParam String keyword) {
        List<SampleResponse> results = List.of(
                new SampleResponse(1L, keyword + "-result", 100_000L, "Matched item", Instant.now()));
        return ApiResponse.success(results);
    }

    // ==================== ERROR SIMULATION ====================

    private void simulateErrorByIdIfNeeded(Long id) {
        switch (id.intValue()) {
            case 0 -> throw new BusinessException(ErrorCode.BAD_REQUEST, "ID must be greater than 0");
            case 404 -> throw new BusinessException(ErrorCode.NOT_FOUND, "Sample with id 404 not found");
            case 401 -> throw new BusinessException(ErrorCode.UNAUTHORIZED, "Authentication required");
            case 403 -> throw new BusinessException(ErrorCode.FORBIDDEN, "You don't have permission to access this resource");
            case 409 -> throw new BusinessException(ErrorCode.CONFLICT, "Sample with this name already exists");
            case 503 -> throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "Downstream service is unavailable");
            case 500 -> throw new RuntimeException("Simulated unhandled exception");
            default -> { }
        }
    }
}
