-- Таблица leads (универсальная для H2)
CREATE TABLE IF NOT EXISTS leads (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT leads_status_check CHECK (status IN ('NEW', 'CONTACTED', 'QUALIFIED', 'LOST'))
);

-- Таблица deals
CREATE TABLE IF NOT EXISTS deals (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    lead_id UUID NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT deals_status_check CHECK (status IN ('NEW', 'QUALIFIED', 'PROPOSAL_SENT', 'NEGOTIATION', 'WON', 'LOST')),
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE RESTRICT
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_leads_email ON leads(email);
CREATE INDEX IF NOT EXISTS idx_leads_status ON leads(status);
CREATE INDEX IF NOT EXISTS idx_deals_lead_id ON deals(lead_id);
