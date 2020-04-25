CREATE TABLE users.account
(
    id                BIGSERIAL PRIMARY KEY,
    created_date      TIMESTAMP   NOT NULL,
    guid              UUID UNIQUE NOT NULL,
    identity_provider BOOLEAN     NOT NULL,
    superuser         BOOLEAN     NOT NULL,
    name              VARCHAR     NOT NULL
);

CREATE TABLE users.user
(
    org_guid          UUID           NOT NULL,
    email_address     VARCHAR UNIQUE NOT NULL,
    first_name        VARCHAR        NOT NULL,
    last_name         VARCHAR        NOT NULL,
    profile_photo_url VARCHAR
) INHERITS (users.account);

CREATE TABLE auth.access_token
(
    id               BIGSERIAL PRIMARY KEY,
    created_date     TIMESTAMP      NOT NULL,
    guid             UUID UNIQUE    NOT NULL,
    account_guid     UUID           NOT NULL,
    encrypted_secret VARCHAR UNIQUE NOT NULL
);
