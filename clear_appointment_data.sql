-- Clear all appointment data and reset sequence
-- This script removes all existing appointment records and resets the ID sequence

-- Step 1: Check current data
SELECT COUNT(*) as current_count FROM lead.appointment;

-- Step 2: Backup current data (optional - uncomment if needed)
-- CREATE TABLE IF NOT EXISTS lead.appointment_backup AS
-- SELECT * FROM lead.appointment;

-- Step 3: Delete all appointment records
DELETE FROM lead.appointment;

-- Step 4: Reset sequence to start from 1
SELECT setval('lead.appointment_seq', 1, false);

-- Step 5: Verify deletion
SELECT COUNT(*) as after_deletion_count FROM lead.appointment;

-- Step 6: Show next ID that will be generated
SELECT nextval('lead.appointment_seq') as next_appointment_id;

-- Step 7: Confirm completion
SELECT 'Appointment data cleared successfully. Next ID will be: ' || nextval('lead.appointment_seq') as status;
