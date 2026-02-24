-- =============================
-- SEEDER USERS (v2)
-- =============================

INSERT INTO users (
    id,
    name,
    email,
    password,
    role,
    created_at
) VALUES (
    '3fa85f64-5717-4562-b3fc-2c963f66afa6',
    'Admin AI Interview',
    'admin@aiinterview.com',
    '$2a$10$7QJj3gYHkW.f9wTnUuZzXe2n0k5fYwqI5OQ9Jx8wJmE1gYkT2d1aG', -- bcrypt password: admin123
    'ADMIN',
    NOW()
);