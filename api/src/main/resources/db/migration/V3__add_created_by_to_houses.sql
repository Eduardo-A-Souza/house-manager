-- Migration: 003_add_created_by_to_houses
-- Adds "created_by" column to houses (who created the house group)
-- This runs AFTER users table exists, to avoid circular reference

ALTER TABLE houses
ADD COLUMN created_by INTEGER REFERENCES users(id) ON DELETE SET NULL;
