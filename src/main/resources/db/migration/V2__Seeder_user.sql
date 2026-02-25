-- =============================
-- SEEDER USERS (v2 COMPLETE)
-- =============================

-- ADMIN
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
    '$2a$10$h.7ZpmXTulEH79xLKoHpCOaueTZVwPkFAnelMg0fNqX0bVquYb6Gm',
    'ADMIN',
    NOW()
);

-- INTERVIEWER
INSERT INTO users (
    id,
    name,
    email,
    password,
    role,
    created_at
) VALUES (
    '7c9e6679-7425-40de-944b-e07fc1f90ae7',
    'HC Fauzy',
    'hc@aiinterview.com',
    '$2a$10$h.7ZpmXTulEH79xLKoHpCOaueTZVwPkFAnelMg0fNqX0bVquYb6Gm',
    'INTERVIEWER',
    NOW()
);

-- CANDIDATE 1
INSERT INTO users (
    id,
    name,
    email,
    password,
    role,
    created_at
) VALUES (
    '1b4e28ba-2fa1-11d2-883f-0016d3cca427',
    'Andi Pratama',
    'andi@aiinterview.com',
    '$2a$10$h.7ZpmXTulEH79xLKoHpCOaueTZVwPkFAnelMg0fNqX0bVquYb6Gm',
    'CANDIDATE',
    NOW()
);

-- CANDIDATE 2
INSERT INTO users (
    id,
    name,
    email,
    password,
    role,
    created_at
) VALUES (
    '9f1c2d3e-4b5a-678c-901d-2e3f4a5b6c7d',
    'Budi Santoso',
    'budi@aiinterview.com',
    '$2a$10$h.7ZpmXTulEH79xLKoHpCOaueTZVwPkFAnelMg0fNqX0bVquYb6Gm',
    'CANDIDATE',
    NOW()
);

-- =============================
-- SEEDER CANDIDATES
-- =============================

INSERT INTO candidates (
    id,
    user_id,
    role,
    experience_years,
    created_at
) VALUES
(
    '11111111-1111-1111-1111-111111111111',
    '1b4e28ba-2fa1-11d2-883f-0016d3cca427',
    'Junior Backend Developer',
    '1',
    NOW()
),
(
    '22222222-2222-2222-2222-222222222222',
    '9f1c2d3e-4b5a-678c-901d-2e3f4a5b6c7d',
    'Fresh Graduate',
    '0',
    NOW()
);