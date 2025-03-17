-- Liquibase formatted SQL

-- changeset wellington:001-create-users-table
CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BINARY(16),
    updated_by BINARY(16)
);


ALTER TABLE users ADD CONSTRAINT fk_users_users_cb FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE users ADD CONSTRAINT fk_users_users_ub FOREIGN KEY (updated_by) REFERENCES users (id);
CREATE UNIQUE INDEX uk_users_email ON users (email);
CREATE UNIQUE INDEX uk_users_user_name ON users (username);


-- changeset wellington:002-create-users-roles-table
CREATE TABLE IF NOT EXISTS users_roles (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BINARY(16),
    updated_by BINARY(16)
);

ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_users_cb FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_users_ub FOREIGN KEY (updated_by) REFERENCES users (id);

-- changeset wellington:003-create-orders-table
CREATE TABLE IF NOT EXISTS orders (
    id BINARY(16) PRIMARY KEY,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    total DECIMAL(10, 4) NOT NULL DEFAULT 0,
    discount DECIMAL(10, 4) NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    created_by BINARY(16),
    updated_by BINARY(16)
);

ALTER TABLE orders ADD CONSTRAINT fk_orders_users_cb FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_users_ub FOREIGN KEY (updated_by) REFERENCES users (id);

-- changeset wellington:004-create-order-items-table
CREATE TABLE IF NOT EXISTS orders_items (
    id BINARY(16) PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    amount BIGINT NOT NULL,
    product VARCHAR(255) NOT NULL,
    sequence SMALLINT NOT NULL,
    created_by BINARY(16),
    updated_by BINARY(16),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
);

ALTER TABLE orders_items ADD CONSTRAINT fk_orders_items_users_cb FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE orders_items ADD CONSTRAINT fk_orders_items_users_ub FOREIGN KEY (updated_by) REFERENCES users (id);
ALTER TABLE orders_items ADD CONSTRAINT fk_orders_items_orders FOREIGN KEY (order_id) REFERENCES orders (id);

-- changeset wellington:005-create-payments-table
CREATE TABLE IF NOT EXISTS payments (
    id BINARY(16) PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    type VARCHAR(30),
    value DECIMAL(10, 4) NOT NULL DEFAULT 0,
    status VARCHAR(30),
    installment SMALLINT NOT NULL DEFAULT 1,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    created_by BINARY(16),
    updated_by BINARY(16)
);

ALTER TABLE payments ADD CONSTRAINT fk_payments_orders FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE payments ADD CONSTRAINT fk_payments_users_cb FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE payments ADD CONSTRAINT fk_payments_users_ub FOREIGN KEY (updated_by) REFERENCES users (id);
CREATE UNIQUE INDEX uk_payments_order_installment ON payments (order_id, installment);
