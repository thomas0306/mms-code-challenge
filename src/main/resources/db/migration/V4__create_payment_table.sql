ALTER TABLE "order"
    ADD COLUMN payment_status CHAR(30);

CREATE TABLE payment (
     id UUID PRIMARY KEY,
     transaction_id VARCHAR(30),
     psp_reference VARCHAR(20),
     status VARCHAR(30),
     currency CHAR(3),
     amount FLOAT,
     order_id UUID,
     created_at TIMESTAMP,
     updated_at TIMESTAMP,
     CONSTRAINT fk_order_payment FOREIGN KEY (order_id) REFERENCES "order"(id)
)
