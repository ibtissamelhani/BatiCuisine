
CREATE TYPE Project_Status AS ENUM ('IN_PROGRESS', 'COMPLETED', 'CANCELED');

CREATE TYPE Component_Type AS ENUM ('MATERIAL', 'LABOR');

CREATE TABLE Clients (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        is_professional BOOLEAN NOT NULL,
                        CONSTRAINT phone_unique UNIQUE (phone)
);

CREATE TABLE Projects (
                         id SERIAL PRIMARY KEY,
                         project_name VARCHAR(100) NOT NULL,
                         profit_margin DOUBLE PRECISION,
                         total_cost DOUBLE PRECISION,
                         project_status Project_Status NOT NULL DEFAULT 'IN_PROGRESS',
                         client_id  INTEGER REFERENCES Clients(id) ON DELETE SET NULL
);
CREATE TABLE Components (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           component_type Component_Type NOT NULL,
                           quantity DOUBLE PRECISION NOT NULL,
                           tax_rate DOUBLE PRECISION,
                           project_id INTEGER REFERENCES Projects(id) ON DELETE CASCADE

);
CREATE TABLE Materials (
                          id SERIAL PRIMARY KEY,
                          unit_cost DOUBLE PRECISION NOT NULL,
                          transport_cost DOUBLE PRECISION,
                          quality_coefficient DOUBLE PRECISION,
                          component_id INTEGER REFERENCES Components(id) ON DELETE CASCADE
);

CREATE TABLE Labors (
                       id SERIAL PRIMARY KEY,
                       hourly_rate DOUBLE PRECISION NOT NULL,
                       work_hours DOUBLE PRECISION NOT NULL,
                       worker_productivity DOUBLE PRECISION,
                       component_id INTEGER REFERENCES Components(id) ON DELETE CASCADE
);

CREATE TABLE Quotes (
                       id SERIAL PRIMARY KEY,
                       estimated_amount DOUBLE PRECISION,
                       issue_date DATE NOT NULL,
                       validity_date DATE NOT NULL,
                       is_accepted BOOLEAN DEFAULT FALSE,
                       project_id INTEGER REFERENCES Projects(id) ON DELETE CASCADE
);

INSERT INTO Clients (name, address, phone, is_professional)
VALUES ('John Doe', '123 Street', '0123456789', TRUE);



