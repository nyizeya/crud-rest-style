CREATE TABLE instructor(
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL ,
    phone VARCHAR(255) NOT NULL UNIQUE ,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE token(
    id BIGINT PRIMARY KEY ,
    expired VARCHAR(5) ,
    revoked VARCHAR(5) ,
    token VARCHAR(255) ,
    user_id BIGINT NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES instructor(id)
);

CREATE TABLE course(
    id BIGINT PRIMARY KEY ,
    description VARCHAR(255) NOT NULL,
    level VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL ,
    start_date DATE NOT NULL ,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES instructor(id)
);