-- Migration: 001_create_houses
-- Creates the "houses" table (family/house group)

CREATE TABLE houses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
