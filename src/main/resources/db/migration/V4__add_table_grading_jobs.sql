-- ================================
-- V3 - Add Table Grading Jobs
-- ================================

CREATE TABLE grading_jobs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    participant_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    retry_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grading_job_participant
        FOREIGN KEY (participant_id) REFERENCES interview_participants(id)
        ON DELETE CASCADE
);