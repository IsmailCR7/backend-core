--liquibase formatted sql
--changeset Ismail:BCORE-32-3

CREATE TABLE contacts (
    id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    lead_id UUID REFERENCES leads(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

