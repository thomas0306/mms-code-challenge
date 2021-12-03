CREATE TABLE "order"
(
    id         UUID PRIMARY KEY,
    status     VARCHAR(30),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
