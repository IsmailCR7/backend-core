-- =============================================
-- Schema для CRM проекта (Sprint 7)
-- Адаптировано под вашу модель данных
-- =============================================

-- Включаем расширение для генерации UUID
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================
-- Таблица: leads
-- =============================================
CREATE TABLE IF NOT EXISTS leads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,

    -- Констрейнт для валидации статусов (соответствует вашему enum)
    CONSTRAINT leads_status_check CHECK (status IN ('NEW', 'CONTACTED', 'QUALIFIED', 'LOST'))
);

-- Комментарии к таблице и колонкам
COMMENT ON TABLE leads IS 'Потенциальные клиенты';
COMMENT ON COLUMN leads.status IS 'Статус лида: NEW, CONTACTED, QUALIFIED, LOST';
COMMENT ON COLUMN leads.created_at IS 'Время создания с временной зоной';

-- Индексы для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_leads_email ON leads(email);
CREATE INDEX IF NOT EXISTS idx_leads_status ON leads(status);
CREATE INDEX IF NOT EXISTS idx_leads_created_at ON leads(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_leads_company ON leads(company);

-- =============================================
-- Таблица: deals (сделки)
-- =============================================
CREATE TABLE IF NOT EXISTS deals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lead_id UUID NOT NULL REFERENCES leads(id) ON DELETE RESTRICT,
    amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,

    -- Констрейнт для валидации статусов сделки
    CONSTRAINT deals_status_check CHECK (status IN (
        'NEW', 'QUALIFIED', 'PROPOSAL_SENT', 'NEGOTIATION', 'WON', 'LOST'
    )),

    -- Констрейнт для положительной суммы
    CONSTRAINT deals_amount_positive CHECK (amount >= 0)
);

-- Комментарии для deals
COMMENT ON TABLE deals IS 'Сделки, конвертированные из лидов';
COMMENT ON COLUMN deals.lead_id IS 'Ссылка на лид (не может быть NULL, т.к. сделка без лида не создается)';
COMMENT ON COLUMN deals.status IS 'Статус сделки: NEW, QUALIFIED, PROPOSAL_SENT, NEGOTIATION, WON, LOST';
COMMENT ON COLUMN deals.amount IS 'Сумма сделки в валюте (положительное число)';

-- Индексы для deals
CREATE INDEX IF NOT EXISTS idx_deals_lead_id ON deals(lead_id);
CREATE INDEX IF NOT EXISTS idx_deals_status ON deals(status);
CREATE INDEX IF NOT EXISTS idx_deals_created_at ON deals(created_at DESC);

-- Составной индекс для частых фильтраций
CREATE INDEX IF NOT EXISTS idx_deals_lead_status ON deals(lead_id, status);

-- =============================================
-- Таблица: contacts (контактные лица)
-- На будущее, пока не активно используется
-- =============================================
CREATE TABLE IF NOT EXISTS contacts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lead_id UUID NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    position VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,

    -- Уникальный email в рамках одного лида
    CONSTRAINT unique_contact_email_per_lead UNIQUE (lead_id, email)
);

-- Индексы для contacts
CREATE INDEX IF NOT EXISTS idx_contacts_lead_id ON contacts(lead_id);
CREATE INDEX IF NOT EXISTS idx_contacts_email ON contacts(email);
CREATE INDEX IF NOT EXISTS idx_contacts_is_primary ON contacts(is_primary) WHERE is_primary = true;

COMMENT ON TABLE contacts IS 'Контактные лица, связанные с лидами';
COMMENT ON COLUMN contacts.is_primary IS 'Основной контакт (только один активный на lead)';

-- =============================================
-- Функция и триггер для автоматического обновления updated_at
-- =============================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Применяем триггеры ко всем таблицам
CREATE TRIGGER update_leads_updated_at
    BEFORE UPDATE ON leads
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_deals_updated_at
    BEFORE UPDATE ON deals
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_contacts_updated_at
    BEFORE UPDATE ON contacts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =============================================
-- Тестовые данные (только для разработки)
-- =============================================

-- Создаем несколько тестовых лидов
INSERT INTO leads (id, email, company, status) VALUES
    (gen_random_uuid(), 'ivan.petrov@example.com', 'ООО ТехноСервис', 'NEW'),
    (gen_random_uuid(), 'elena.smirnova@example.com', 'Альфа Групп', 'CONTACTED'),
    (gen_random_uuid(), 'mikhail.ivanov@example.com', 'Бета Корп', 'QUALIFIED'),
    (gen_random_uuid(), 'anna.sidorova@example.com', 'Гамма Лтд', 'NEW'),
    (gen_random_uuid(), 'dmitry.kozlov@example.com', 'Дельта Инк', 'LOST')
ON CONFLICT (email) DO NOTHING;

-- Добавляем тестовые сделки для существующих лидов
-- Сначала получим ID существующих лидов (через подзапрос)
INSERT INTO deals (lead_id, amount, status)
SELECT
    l.id,
    CASE
        WHEN l.status = 'QUALIFIED' THEN 50000.00
        WHEN l.status = 'CONTACTED' THEN 25000.00
        ELSE 10000.00
    END as amount,
    CASE
        WHEN l.status = 'QUALIFIED' THEN 'QUALIFIED'
        WHEN l.status = 'CONTACTED' THEN 'NEW'
        ELSE 'NEW'
    END as status
FROM leads l
WHERE NOT EXISTS (
    SELECT 1 FROM deals d WHERE d.lead_id = l.id
)
LIMIT 3;

-- =============================================
-- Полезные представления (views)
-- =============================================

-- Представление: сводка по лидам и сделкам
CREATE OR REPLACE VIEW lead_deal_summary AS
SELECT
    l.id as lead_id,
    l.email,
    l.company,
    l.status as lead_status,
    d.id as deal_id,
    d.amount as deal_amount,
    d.status as deal_status,
    d.created_at as deal_created_at
FROM leads l
LEFT JOIN deals d ON l.id = d.lead_id;

COMMENT ON VIEW lead_deal_summary IS 'Сводка по лидам и их сделкам';

-- =============================================
-- Функция для подсчета статистики
-- =============================================
CREATE OR REPLACE FUNCTION get_leads_statistics()
RETURNS TABLE(
    total_leads BIGINT,
    new_leads BIGINT,
    contacted_leads BIGINT,
    qualified_leads BIGINT,
    lost_leads BIGINT,
    total_deals_amount DECIMAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(DISTINCT l.id)::BIGINT as total_leads,
        COUNT(DISTINCT CASE WHEN l.status = 'NEW' THEN l.id END)::BIGINT as new_leads,
        COUNT(DISTINCT CASE WHEN l.status = 'CONTACTED' THEN l.id END)::BIGINT as contacted_leads,
        COUNT(DISTINCT CASE WHEN l.status = 'QUALIFIED' THEN l.id END)::BIGINT as qualified_leads,
        COUNT(DISTINCT CASE WHEN l.status = 'LOST' THEN l.id END)::BIGINT as lost_leads,
        COALESCE(SUM(d.amount), 0) as total_deals_amount
    FROM leads l
    LEFT JOIN deals d ON l.id = d.lead_id;
END;
$$ LANGUAGE plpgsql;