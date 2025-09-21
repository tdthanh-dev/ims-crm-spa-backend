-- Migration script: Update Appointment table from complex booking to simple reminder
-- This script migrates existing appointments to the new simplified schema

-- Step 1: Check current schema
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'appointment' AND table_schema = 'lead';

-- Step 2: Create backup of existing data (if needed)
-- CREATE TABLE lead.appointment_backup AS
-- SELECT * FROM lead.appointment;

-- Step 3: Drop old indexes that reference non-existent columns
DROP INDEX IF EXISTS lead.idx_appt_start;
DROP INDEX IF EXISTS lead.idx_appt_tech_time;

-- Step 4: Add new columns (if they don't exist)
-- appointment_date (LocalDate) - NOT NULL
-- appointment_time (LocalTime) - NULLABLE
-- customer_name (VARCHAR 200) - NULLABLE
-- customer_phone (VARCHAR 20) - NULLABLE

ALTER TABLE lead.appointment
ADD COLUMN IF NOT EXISTS appointment_date DATE NOT NULL DEFAULT CURRENT_DATE,
ADD COLUMN IF NOT EXISTS appointment_time TIME,
ADD COLUMN IF NOT EXISTS customer_name VARCHAR(200),
ADD COLUMN IF NOT EXISTS customer_phone VARCHAR(20);

-- Step 5: Migrate data from old columns to new columns
-- Convert startAt to appointment_date
UPDATE lead.appointment
SET appointment_date = DATE(start_at),
    appointment_time = TIME(start_at)
WHERE start_at IS NOT NULL;

-- Migrate customer info if available
-- Note: If you have existing customer references, this would be more complex
-- For now, we'll keep the existing data structure

-- Step 6: Drop old columns (optional - keep for now in case of rollback)
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS start_at;
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS end_at;
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS technician_id;
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS receptionist_id;
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS service_id;

-- Step 7: Create new indexes for the new schema
CREATE INDEX IF NOT EXISTS idx_appt_date ON lead.appointment (appointment_date);
CREATE INDEX IF NOT EXISTS idx_appt_phone ON lead.appointment (customer_phone);
CREATE INDEX IF NOT EXISTS idx_appt_status ON lead.appointment (status);
CREATE INDEX IF NOT EXISTS idx_appt_customer ON lead.appointment (customer_id);
CREATE INDEX IF NOT EXISTS idx_appt_lead ON lead.appointment (lead_id);

-- Step 8: Update constraints
-- Make appointment_date NOT NULL (add constraint if needed)
ALTER TABLE lead.appointment
ALTER COLUMN appointment_date SET NOT NULL;

-- Step 9: Verify migration
SELECT
    appt_id,
    appointment_date,
    appointment_time,
    customer_name,
    customer_phone,
    status,
    note
FROM lead.appointment
LIMIT 10;

-- Step 10: Count appointments by date (test new schema)
SELECT appointment_date, COUNT(*) as count
FROM lead.appointment
GROUP BY appointment_date
ORDER BY appointment_date DESC
LIMIT 5;

-- Migration completed!
-- The appointment table now uses the simplified schema:
-- - appointment_date (DATE) instead of start_at/end_at
-- - appointment_time (TIME) for optional time
-- - customer_name and customer_phone for direct storage
-- - Simplified structure suitable for basic reminders
