create table users (
    id uuid not null unique,
    email varchar(255) not null unique,
    sanitized_email varchar(255) not null,
    last_modified_date timestamp(6) not null,
    created_date timestamp(6) not null,
    primary key (id)
);

CREATE INDEX idx_email ON users (email);
CREATE INDEX idx_sanitized_email ON users (sanitized_email);