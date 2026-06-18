--liquibase formatted sql
--changeset Ismail:BCORE-32-4

CREATE TABLE deals (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2),
    stage VARCHAR(50) NOT NULL,
    lead_id UUID REFERENCES leads(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
