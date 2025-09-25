-- postgresql database initialization

-- Step 1: Create enum type
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
CREATE TYPE study_time_enum AS ENUM ('MORNING', 'AFTERNOON', 'EVENING', 'NIGHT');
CREATE TYPE level AS ENUM ('BEGINNER','INTERMEDIATE','ADVANCED');
-- Step 2: Create table using enum type
CREATE TABLE "users" (
    id varchar(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role user_role NOT NULL,
    fullname VARCHAR(100),
    avartar_url TEXT,
    public_id VARCHAR(255),
    target INT,
    study_time study_time_enum,
    level level,
    -- interests JSONB, create a separate table for interests
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);