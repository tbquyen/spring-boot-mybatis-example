CREATE TABLE accounts (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(100) NOT NULL,
  status INT NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT NOW(),
  lastLogin TIMESTAMP
);

CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE questions (
  id SERIAL PRIMARY KEY,
  category_id INT NOT NULL REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE,
  content TEXT NOT NULL,
  explanation TEXT,
  type VARCHAR(100) NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE answers (
  id SERIAL PRIMARY KEY,
  question_id INT NOT NULL REFERENCES questions(id) ON DELETE CASCADE ON UPDATE CASCADE,
  content TEXT NOT NULL,
  correct BOOLEAN NOT NULL DEFAULT FALSE,
  created TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE quiz (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  max_question INT NOT NULL,
  min_question INT NOT NULL,
  remark TEXT NULL,
  duration INT NOT NULL DEFAULT 30,
  created TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE quiz_info (
  id SERIAL PRIMARY KEY,
  quiz_id INT NOT NULL REFERENCES quiz(id) ON DELETE CASCADE ON UPDATE CASCADE,
  accounts_id INT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE ON UPDATE CASCADE,
  time_start TIMESTAMP NOT NULL DEFAULT NOW(),
  time_end TIMESTAMP NOT NULL DEFAULT NOW(),
  remark TEXT NULL,
  created TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE quiz_questions (
  quiz_info_id INT NOT NULL REFERENCES quiz_info(id) ON DELETE CASCADE ON UPDATE CASCADE,
  question_id INT NOT NULL REFERENCES questions(id) ON DELETE CASCADE ON UPDATE CASCADE,
  created TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY(quiz_info_id, question_id)
);

CREATE TABLE quiz_answers (
  quiz_info_id INT NOT NULL REFERENCES quiz_info(id) ON DELETE CASCADE ON UPDATE CASCADE,
  question_id INT NOT NULL REFERENCES questions(id) ON DELETE CASCADE ON UPDATE CASCADE,
  answers_id INT NOT NULL REFERENCES answers(id) ON DELETE CASCADE ON UPDATE CASCADE,
  created TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY(quiz_info_id, question_id, answers_id)
);

INSERT INTO accounts(username, password, role, status)
VALUES ('admin', '$2a$10$Z4EVgFqddDr44UepvEWikufVNSeiy.EqWkyzLoRYpEMys4p6tn8PC', 'ROLE_ADMIN', 0);