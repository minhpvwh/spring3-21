package vn.tpbank.service.sample.model.dto;

import java.time.Instant;

public class SampleResponse {

    private Long id;
    private String name;
    private Long amount;
    private String description;
    private Instant createdAt;

    public SampleResponse() {
    }

    public SampleResponse(Long id, String name, Long amount, String description, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
