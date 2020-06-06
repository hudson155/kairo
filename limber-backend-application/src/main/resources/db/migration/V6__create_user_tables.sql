CREATE TABLE users.account
(
    id                BIGSERIAL PRIMARY KEY,
    guid              UUID UNIQUE NOT NULL,
    created_date      TIMESTAMP   NOT NULL,
    archived_date     TIMESTAMP DEFAULT NULL,
    identity_provider BOOLEAN     NOT NULL,
    superuser         BOOLEAN     NOT NULL,
    name              VARCHAR     NOT NULL
);

CREATE TABLE users.user
(
    org_guid          UUID    NOT NULL,
    email_address     VARCHAR NOT NULL,
    first_name        VARCHAR NOT NULL,
    last_name         VARCHAR NOT NULL,
    profile_photo_url VARCHAR
) INHERITS (users.account);

CREATE UNIQUE INDEX
    ON users.user (org_guid, LOWER(email_address))
    WHERE archived_date IS NULL;
