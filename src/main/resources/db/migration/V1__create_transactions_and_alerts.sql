-- Transactions received for monitoring
CREATE TABLE transactions (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_ref       VARCHAR(64)    NOT NULL UNIQUE,
    account_id            VARCHAR(34)    NOT NULL,
    counterparty_account  VARCHAR(34),
    counterparty_country  VARCHAR(2),
    amount                NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
    currency              VARCHAR(3)     NOT NULL,
    direction             VARCHAR(6)     NOT NULL CHECK (direction IN ('DEBIT', 'CREDIT')),
    booked_at             TIMESTAMPTZ    NOT NULL,
    created_at            TIMESTAMPTZ    NOT NULL DEFAULT now()
);

-- Supports per-account look-backs, such as a velocity rule
CREATE INDEX idx_transactions_account_booked ON transactions (account_id, booked_at);

-- Alerts raised by monitoring rules, for investigation
CREATE TABLE alerts (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id  BIGINT       NOT NULL REFERENCES transactions (id),
    rule_code       VARCHAR(50)  NOT NULL,
    severity        VARCHAR(10)  NOT NULL CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH')),
    status          VARCHAR(20)  NOT NULL DEFAULT 'OPEN'
                    CHECK (status IN ('OPEN', 'UNDER_REVIEW', 'ESCALATED', 'CLOSED')),
    reason          VARCHAR(500) NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    -- A rule can only raise one alert per transaction, so reprocessing is safe
    CONSTRAINT uq_alert_per_rule UNIQUE (transaction_id, rule_code)
);

-- Supports the investigator work queue: open alerts, newest first
CREATE INDEX idx_alerts_status_created ON alerts (status, created_at);
