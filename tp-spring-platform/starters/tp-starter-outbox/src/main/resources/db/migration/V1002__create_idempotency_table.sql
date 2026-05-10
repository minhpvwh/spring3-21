CREATE TABLE TP_IDEMPOTENCY_KEY (
    idempotency_key VARCHAR2(255) PRIMARY KEY,
    response_body   CLOB,
    response_status NUMBER(5),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
