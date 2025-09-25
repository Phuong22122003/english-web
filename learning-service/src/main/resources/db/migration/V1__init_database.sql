-- postgresql database initialization

-- Step 1: Create enum type
CREATE TYPE user_role AS ENUM ('admin', 'user');
CREATE TYPE study_time_enum AS ENUM ('less_than_1_hour', '1_to_2_hours', '2_to_3_hours', 'more_than_3_hours');

-- vocabulary, grammar, listening
CREATE TYPE item_type_enum AS ENUM ('vocabulary', 'grammar', 'listening', 'full_test');

-- Step 2: Create table using enum type
CREATE TABLE user (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role user_role NOT NULL,
    fullname VARCHAR(100),
    avartar_url TEXT,
    target INT,
    study_time study_time_enum,
    level VARCHAR(50),
    interests JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

CREATE TABLE vocabulary_topic(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vocabulary(
    id UUID PRIMARY KEY,
    topic_id UUID REFERENCES vocabulary_topic(id) ON DELETE CASCADE,
    word VARCHAR(100) NOT NULL,
    phonetic VARCHAR(100),
    meaning TEXT,
    example TEXT,
    audio_url TEXT,
    image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vocabulary_test(
    id UUID PRIMARY KEY,
    topic_id UUID REFERENCES vocabulary_topic(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE vocabulary_test_question(
    id UUID PRIMARY KEY,
    test_id UUID REFERENCES vocabulary_test(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    options JSONB NOT NULL,
    correct_answer VARCHAR(100) NOT NULL
);

CREATE TABLE grammar_topic(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE grammar(
    id UUID PRIMARY KEY,
    topic_id UUID REFERENCES grammar_topic(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grammar_test(
    id UUID PRIMARY KEY,
    grammar_id UUID REFERENCES grammar(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    options JSONB NOT NULL,
    correct_answer VARCHAR(100) NOT NULL
);

CREATE TABLE listening_topic(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE listening(
    id UUID PRIMARY KEY,
    topic_id UUID REFERENCES listening_topic(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    audio_url TEXT NOT NULL,
    image_url TEXT,
    transcript TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE listening_test(
    id UUID PRIMARY KEY,
    topic_id UUID REFERENCES listening_topic(id) ON DELETE CASCADE,
    audio_url TEXT NOT NULL,
    image_url TEXT,
    question TEXT NOT NULL,
    options JSONB NOT NULL,
    correct_answer VARCHAR(100) NOT NULL,
    explaination TEXT
);

CREATE TABLE exam_history(
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES user(id) ON DELETE CASCADE,
    exam_type item_type_enum NOT NULL,
    exam_id UUID NOT NULL,
    score INT NOT NULL,
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_answer(
    id UUID PRIMARY KEY,
    exam_history_id UUID REFERENCES exam_history(id) ON DELETE CASCADE,
    question_id UUID NOT NULL,
    selected_answer VARCHAR(10) NOT NULL,
    is_correct BOOLEAN NOT NULL
);


CREATE TABLE favorites(
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES user(id) ON DELETE CASCADE,
    item_type item_type_enum NOT NULL,
    item_id UUID NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE plan(
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES user(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    is_completed BOOLEAN DEFAULT FALSE,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    end_date TIMESTAMP,
    target INT
);

CREATE TABLE plan_detail(
    id UUID PRIMARY KEY,
    plan_id UUID REFERENCES plan(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    item_type item_type_enum NOT NULL,
    item_id UUID,
    is_completed BOOLEAN DEFAULT FALSE
);