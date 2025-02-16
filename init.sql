CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    balance DECIMAL(10, 2),
    version BIGINT
);

INSERT INTO wallets (balance, version) VALUES (100.00, 1), (250.50, 1);
