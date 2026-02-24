CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- INTERVIEWER, CANDIDATE, ADMIN
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- INTERVIEWS
CREATE TABLE interviews (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    context TEXT NOT NULL,
    objective TEXT NOT NULL,
    role_target VARCHAR(100) NOT NULL,
    level_target VARCHAR(100) NOT NULL,
    technology VARCHAR(100) NOT NULL,
    purpose VARCHAR(50) NOT NULL, -- INTERNAL_ASSESSMENT, HIRING
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_by UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_interview_user
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE CASCADE
);

-- CANDIDATES
CREATE TABLE candidates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL UNIQUE,
    current_level VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_candidate_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- INTERVIEW PARTICIPANTS
CREATE TABLE interview_participants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    interview_id UUID NOT NULL,
    candidate_id UUID NOT NULL,
    total_score FLOAT,
    final_recommendation VARCHAR(100),
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    CONSTRAINT fk_participant_interview
        FOREIGN KEY (interview_id) REFERENCES interviews(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_participant_candidate
        FOREIGN KEY (candidate_id) REFERENCES candidates(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_interview_candidate
        UNIQUE (interview_id, candidate_id)
);

-- QUESTIONS
CREATE TABLE questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    interview_id UUID NOT NULL,
    question_text TEXT NOT NULL,
    order_number INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_interview
        FOREIGN KEY (interview_id) REFERENCES interviews(id)
        ON DELETE CASCADE
);

-- ANSWERS
CREATE TABLE answers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    participant_id UUID NOT NULL,
    question_id UUID NOT NULL,
    audio_path VARCHAR(500),
    transcript TEXT,
    score FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_participant
        FOREIGN KEY (participant_id) REFERENCES interview_participants(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_answer_question
        FOREIGN KEY (question_id) REFERENCES questions(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_answer_per_question
        UNIQUE (participant_id, question_id)
);

-- AI PROCESSING LOGS
CREATE TABLE ai_processing_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    answer_id UUID NOT NULL,
    stt_status VARCHAR(50),
    scoring_status VARCHAR(50),
    error_message TEXT,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_answer
        FOREIGN KEY (answer_id) REFERENCES answers(id)
        ON DELETE CASCADE
);