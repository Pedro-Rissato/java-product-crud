CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL,
    description VARCHAR(500) NOT NULL,
    discount_percentage DECIMAL(5,2) DEFAULT 0.00
);