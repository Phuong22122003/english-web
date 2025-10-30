CREATE TYPE topic_type AS ENUM ('VOCABULARY', 'GRAMMAR', 'LISTENING');

CREATE TABLE topic_view_statistic(
     id VARCHAR(36) PRIMARY KEY,
     topic_id VARCHAR(36) NOT NULL,
     topic_type topic_type NOT NULL,
     view_count INT NOT NULL,
     view_date DATE  NOT NULL,
     view_hour INT NOT NULL CHECK (view_hour BETWEEN 0 AND 23),
     UNIQUE (topic_id, view_date, view_hour)
);