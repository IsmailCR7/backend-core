--liquibase formatted sql
--changeset Ismail:BCORE-32-2

CREATE TABLE leads (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50) NOT NULL,
    company_id UUID REFERENCES companies(id),
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL
);
