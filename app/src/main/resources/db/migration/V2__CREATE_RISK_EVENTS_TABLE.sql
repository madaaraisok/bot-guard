create table risk_events (
    id uuid not null unique,
    status enum ('FAILED','SUCCEEDED') not null,
    type enum ('LOGIN','REGISTRATION') not null,
    ip varchar(255) not null,
    lat float(53) not null,
    lon float(53) not null,
    user_id uuid not null,
    last_modified_date timestamp(6) not null,
    created_date timestamp(6) not null,
    primary key (id),
    foreign key (user_id) references users
);

CREATE INDEX idx_prev_event ON risk_events (ip, user_id, created_date);