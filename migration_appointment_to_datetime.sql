-- Migration: Update appointment table from separate date/time to combined datetime
-- This script migrates existing appointments to use LocalDateTime instead of separate LocalDate and LocalTime

-- Step 1: Check current schema
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'appointment' AND table_schema = 'lead'
ORDER BY ordinal_position;

-- Step 2: Add new column for combined datetime
ALTER TABLE lead.appointment
ADD COLUMN IF NOT EXISTS appointment_date_time TIMESTAMPTZ;

-- Step 3: Migrate data from separate columns to combined column
UPDATE lead.appointment
SET appointment_date_time = CASE
    WHEN appointment_date IS NOT NULL AND appointment_time IS NOT NULL THEN
        appointment_date::date + appointment_time::time
    WHEN appointment_date IS NOT NULL THEN
        appointment_date::date + TIME '00:00:00'
    ELSE
        CURRENT_TIMESTAMP
END
WHERE appointment_date_time IS NULL;

-- Step 4: Make the new column NOT NULL (after data migration)
ALTER TABLE lead.appointment
ALTER COLUMN appointment_date_time SET NOT NULL;

-- Step 5: Drop old columns (optional - uncomment if you want to clean up)
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS appointment_date;
-- ALTER TABLE lead.appointment DROP COLUMN IF EXISTS appointment_time;

-- Step 6: Update indexes - Drop old index and create new one
DROP INDEX IF EXISTS lead.idx_appt_date;
CREATE INDEX IF NOT EXISTS idx_appt_datetime ON lead.appointment (appointment_date_time);

-- Step 7: Verify migration
SELECT
    COUNT(*) as total_records,
    COUNT(appointment_date_time) as migrated_records,
    COUNT(CASE WHEN appointment_date_time IS NOT NULL THEN 1 END) as valid_datetime
FROM lead.appointment;

-- Step 8: Show sample data
SELECT
    appt_id,
    appointment_date_time,
    status,
    customer_name,
    customer_phone
FROM lead.appointment
ORDER BY appointment_date_time DESC
LIMIT 5;

-- Migration completed!
-- The appointment table now uses combined LocalDateTime instead of separate LocalDate and LocalTime
