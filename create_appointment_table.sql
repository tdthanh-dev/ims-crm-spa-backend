-- Create appointment table with new simplified schema
-- This creates the appointment table from scratch with the new simplified structure

-- Create sequence for appointment ID
CREATE SEQUENCE IF NOT EXISTS lead.appointment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Create appointment table
CREATE TABLE IF NOT EXISTS lead.appointment (
    -- Primary key
    appt_id BIGINT PRIMARY KEY DEFAULT nextval('lead.appointment_seq'),

    -- Flexible references (can be null, one, or both)
    lead_id BIGINT,
    customer_id BIGINT,

    -- Basic customer info for reminders (stored directly)
    customer_name VARCHAR(200),
    customer_phone VARCHAR(20),

    -- Simple appointment date and time (combined)
    appointment_date_time TIMESTAMPTZ NOT NULL,

    -- Status and note
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    note TEXT,

    -- Tracking fields
    reminder_sent BOOLEAN DEFAULT FALSE,
    confirmed_at TIMESTAMPTZ,
    cancelled_reason TEXT,

    -- Audit fields (inherited from BaseEntity)
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_appt_datetime ON lead.appointment (appointment_date_time);
CREATE INDEX IF NOT EXISTS idx_appt_phone ON lead.appointment (customer_phone);
CREATE INDEX IF NOT EXISTS idx_appt_status ON lead.appointment (status);
CREATE INDEX IF NOT EXISTS idx_appt_customer ON lead.appointment (customer_id);
CREATE INDEX IF NOT EXISTS idx_appt_lead ON lead.appointment (lead_id);

-- Add comments for documentation
COMMENT ON TABLE lead.appointment IS 'Simplified appointment table for basic reminders';
COMMENT ON COLUMN lead.appointment.appointment_date_time IS 'The appointment date and time (required)';
COMMENT ON COLUMN lead.appointment.customer_name IS 'Customer name stored directly for reminders';
COMMENT ON COLUMN lead.appointment.customer_phone IS 'Customer phone stored directly for reminders';

-- Verify table creation
SELECT
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'appointment'
    AND table_schema = 'lead'
ORDER BY ordinal_position;
