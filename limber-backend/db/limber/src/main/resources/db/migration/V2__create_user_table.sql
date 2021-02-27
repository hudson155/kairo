CREATE TABLE users.user
(
    guid              UUID        NOT NULL,
    created_date      TIMESTAMPTZ NOT NULL,
    permissions       VARCHAR     NOT NULL,
    org_guid          UUID        NOT NULL,
    email_address     VARCHAR     NOT NULL,
    first_name        VARCHAR     NOT NULL,
    last_name         VARCHAR     NOT NULL,
    profile_photo_url VARCHAR
);

CREATE UNIQUE INDEX uniq__user__guid
    ON users.user (guid);

CREATE UNIQUE INDEX uniq__user__email_address
    ON users.user (org_guid, LOWER(email_address));
