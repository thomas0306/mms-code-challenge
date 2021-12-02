CREATE TABLE cart (
  id UUID PRIMARY KEY,
  currency CHAR(3),
  total_price FLOAT,
  discounted_price FLOAT,
  shipping_price FLOAT,
  order_id UUID,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT fk_order_cart FOREIGN KEY (order_id) REFERENCES "order"(id)
);

CREATE TABLE item (
  id UUID PRIMARY KEY,
  item_id VARCHAR(20),
  unit_price FLOAT,
  quantity SMALLINT,
  cart_id UUID,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT  fk_cart_item FOREIGN KEY (cart_id) REFERENCES cart(id)
)
