-- ================================
-- V3 - Add STT Job Columns
-- ================================

-- Tambahkan kolom status
ALTER TABLE answers
ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PENDING';

-- Tambahkan kolom retry_count
ALTER TABLE answers
ADD COLUMN retry_count INT NOT NULL DEFAULT 0;

-- Optional: index untuk performa worker query
CREATE INDEX idx_answers_status_created_at
ON answers(status, created_at);