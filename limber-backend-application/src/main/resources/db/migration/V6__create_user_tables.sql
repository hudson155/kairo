CREATE TABLE users.account
(
    guid              UUID      NOT NULL,
    created_date      TIMESTAMP NOT NULL,
    identity_provider BOOLEAN   NOT NULL,
    superuser         BOOLEAN   NOT NULL,
    name              VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX uniq__account__guid
    ON users.account (guid);

CREATE TABLE users.user
(
    org_guid          UUID    NOT NULL,
    email_address     VARCHAR NOT NULL,
    first_name        VARCHAR NOT NULL,
    last_name         VARCHAR NOT NULL,
    profile_photo_url VARCHAR
) INHERITS (users.account);

CREATE UNIQUE INDEX uniq__user__email_address
    ON users.user (org_guid, LOWER(email_address));
