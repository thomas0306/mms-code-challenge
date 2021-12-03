CREATE TABLE shipment
(
    id                    UUID PRIMARY KEY,
    carrier               VARCHAR(30),
    tracking_number       VARCHAR(20),
    status                VARCHAR(30),
    order_id              UUID,
    estimated_delivery_at TIMESTAMP,
    actual_delivery_at    TIMESTAMP,
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP,
    CONSTRAINT fk_order_shipment FOREIGN KEY (order_id) REFERENCES "order" (id)
);
