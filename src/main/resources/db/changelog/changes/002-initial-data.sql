-- Liquibase formatted SQL

-- changeset wellington:001-insert-admin-user
begin;

INSERT INTO users (id, name, user_name, email, password) VALUES
    (UNHEX(REPLACE(UUID(), '-', '')), 'Admin User', 'admin', 'admin@example.com', 'hashed_password_here');

INSERT INTO users_roles (id, user_id, role) VALUES
    (UNHEX(REPLACE(UUID(), '-', '')), (SELECT id FROM users WHERE user_name = 'admin'), 'ADMIN');

commit;