-- Migration: 005_create_task_history
-- Creates the "task_history" table (optional feature: track task actions)

CREATE TABLE task_history (
    id SERIAL PRIMARY KEY,
    task_id INTEGER NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    action VARCHAR(20) NOT NULL CHECK (action IN ('created', 'completed', 'edited', 'deleted')),
    user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
    "timestamp" TIMESTAMP NOT NULL DEFAULT NOW()
);
