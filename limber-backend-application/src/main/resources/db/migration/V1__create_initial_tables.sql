CREATE SCHEMA users;

CREATE TABLE users.account
(
    id                BIGSERIAL PRIMARY KEY,
    created_date      TIMESTAMP   NOT NULL,
    guid              UUID UNIQUE NOT NULL,
    name              VARCHAR     NOT NULL,
    identity_provider BOOLEAN     NOT NULL,
    superuser         BOOLEAN     NOT NULL
);

CREATE TABLE users.user
(
    id                BIGSERIAL PRIMARY KEY,
    created_date      TIMESTAMP      NOT NULL,
    account_guid      UUID UNIQUE    NOT NULL REFERENCES users.account (guid) ON DELETE CASCADE,
    email_address     VARCHAR UNIQUE NOT NULL,
    first_name        VARCHAR        NOT NULL,
    last_name         VARCHAR        NOT NULL,
    profile_photo_url VARCHAR
);

CREATE SCHEMA auth;

CREATE TABLE auth.access_token
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP      NOT NULL,
    guid         UUID UNIQUE    NOT NULL,
    account_guid UUID           NOT NULL,
    token        VARCHAR UNIQUE NOT NULL
);

CREATE SCHEMA orgs;

CREATE TABLE orgs.org
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP   NOT NULL,
    guid         UUID UNIQUE NOT NULL,
    name         VARCHAR     NOT NULL
);

CREATE TABLE orgs.feature
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP   NOT NULL,
    guid         UUID UNIQUE NOT NULL,
    org_guid     UUID        NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    name         VARCHAR     NOT NULL,
    path         VARCHAR     NOT NULL,
    type         VARCHAR     NOT NULL,
    UNIQUE (org_guid, path)
);

CREATE TABLE orgs.membership
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP NOT NULL,
    org_guid     UUID      NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    account_guid UUID      NOT NULL,
    UNIQUE (org_guid, account_guid)
);

CREATE SCHEMA forms;

CREATE TABLE forms.form_template
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP   NOT NULL,
    guid         UUID UNIQUE NOT NULL,
    org_guid     UUID        NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    title        VARCHAR     NOT NULL,
    description  VARCHAR
);

CREATE TABLE forms.form_template_question
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    guid               UUID UNIQUE NOT NULL,
    form_template_guid UUID        NOT NULL REFERENCES forms.form_template (guid) ON DELETE CASCADE,
    label              VARCHAR     NOT NULL,
    help_text          VARCHAR,
    description        VARCHAR
);
