-- Создание таблицы leads
CREATE TABLE IF NOT EXISTS leads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT leads_status_check CHECK (status IN ('NEW', 'CONTACTED', 'QUALIFIED', 'LOST'))
);

-- Создание таблицы deals
CREATE TABLE IF NOT EXISTS deals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lead_id UUID NOT NULL REFERENCES leads(id) ON DELETE RESTRICT,
    amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT deals_status_check CHECK (status IN ('NEW', 'QUALIFIED', 'PROPOSAL_SENT', 'NEGOTIATION', 'WON', 'LOST'))
);

-- Индексы для оптимизации
CREATE INDEX IF NOT EXISTS idx_leads_email ON leads(email);
CREATE INDEX IF NOT EXISTS idx_leads_status ON leads(status);
CREATE INDEX IF NOT EXISTS idx_deals_lead_id ON deals(lead_id);

-- Тестовые данные
INSERT INTO leads (email, company, status) VALUES
    ('ivan.petrov@example.com', 'ООО ТехноСервис', 'NEW'),
    ('elena.smirnova@example.com', 'Альфа Групп', 'CONTACTED'),
    ('mikhail.ivanov@example.com', 'Бета Корп', 'QUALIFIED')
ON CONFLICT (email) DO NOTHING;
