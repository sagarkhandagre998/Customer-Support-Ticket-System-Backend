-- Roles Table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Seed roles
INSERT INTO roles (name) VALUES
  ('ROLE_USER'),
  ('ROLE_AGENT'),
  ('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;


-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT REFERENCES roles(id)
);


-- Tickets Table
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50),
    category VARCHAR(50),
    owner_id BIGINT REFERENCES users(id),
    assignee_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    resolved_at TIMESTAMP DEFAULT NOW()
);


-- Ticket Comments Table
CREATE TABLE ticket_comments (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    ticket_id BIGINT REFERENCES tickets(id),
    user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW()
);


-- Ratings Table
CREATE TABLE rating (
    id BIGSERIAL PRIMARY KEY,
    stars INT NOT NULL CHECK (stars BETWEEN 1 AND 5),
    feedback TEXT,
    ticket_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_rating_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
