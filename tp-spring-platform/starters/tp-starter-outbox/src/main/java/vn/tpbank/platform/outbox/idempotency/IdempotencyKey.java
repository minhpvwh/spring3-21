package vn.tpbank.platform.outbox.idempotency;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "TP_IDEMPOTENCY_KEY")
public class IdempotencyKey {

    @Id
    @Column(name = "idempotency_key", length = 255)
    private String key;

    @Column(name = "response_body", columnDefinition = "CLOB")
    private String responseBody;

    @Column(name = "response_status")
    private int responseStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public int getResponseStatus() { return responseStatus; }
    public void setResponseStatus(int responseStatus) { this.responseStatus = responseStatus; }
    public Instant getCreatedAt() { return createdAt; }
}
