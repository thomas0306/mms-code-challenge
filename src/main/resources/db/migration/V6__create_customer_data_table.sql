CREATE TABLE customer_data
(
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(30),
    last_name     VARCHAR(30),
    phone_number  VARCHAR(30),
    email_address VARCHAR(50),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP
);

CREATE TABLE order_customer_data
(
    id               UUID PRIMARY KEY,
    order_id         UUID,
    customer_data_id UUID,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    CONSTRAINT fk_order_order_customer_data FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT fk_customer_data_order_customer_data FOREIGN KEY (customer_data_id) REFERENCES customer_data (id)
);

CREATE TABLE address
(
    id               UUID PRIMARY KEY,
    roles            TEXT[],
    street           VARCHAR(50),
    street_number    VARCHAR(10),
    zip_code         VARCHAR(15),
    city             VARCHAR(50),
    country          VARCHAR(50),
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    customer_data_id UUID
);
