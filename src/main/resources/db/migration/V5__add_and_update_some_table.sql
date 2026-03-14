-- ================================
-- V5 - Add Table Category Score and Update Table Answer & Interview Participant
-- ================================

-- Add Table Category Scores
CREATE TABLE category_scores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ADD Seeder Category Score
INSERT INTO category_scores (category, score) VALUES
('Technical Fundamental', 50),
('Problem Solving', 30),
('Communication', 20);

-- Update Table Answer
ALTER TABLE answers
DROP COLUMN score,
ADD COLUMN technical_fundamental_score FLOAT,
ADD COLUMN communication_score FLOAT,
ADD COLUMN problem_solving_score FLOAT,
ADD COLUMN break_time VARCHAR(20),
ADD COLUMN answer_time VARCHAR(20);

-- Update Table Interview Participant
ALTER TABLE interview_participants RENAME COLUMN final_recommendation TO recommendation;
ALTER TABLE interview_participants ADD COLUMN summary_reason TEXT;