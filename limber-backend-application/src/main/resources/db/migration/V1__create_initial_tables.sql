CREATE SCHEMA users;

CREATE TYPE users.ACCOUNT_ROLE AS ENUM ('IDENTITY_PROVIDER', 'SUPERUSER');

CREATE TABLE users.account
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP            NOT NULL DEFAULT now(),
    modified_date TIMESTAMP            NOT NULL DEFAULT now(),
    guid          UUID UNIQUE          NOT NULL,
    name          VARCHAR              NOT NULL,
    roles         users.ACCOUNT_ROLE[] NOT NULL
);

CREATE TABLE users.user
(
    id                BIGSERIAL PRIMARY KEY,
    created_date      TIMESTAMP      NOT NULL DEFAULT now(),
    modified_date     TIMESTAMP      NOT NULL DEFAULT now(),
    account_id        BIGINT UNIQUE  NOT NULL REFERENCES users.account (id) ON DELETE CASCADE,
    email_address     VARCHAR UNIQUE NOT NULL,
    first_name        VARCHAR        NOT NULL,
    last_name         VARCHAR        NOT NULL,
    profile_photo_url VARCHAR
);

CREATE SCHEMA auth;

CREATE TABLE auth.access_token
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP      NOT NULL DEFAULT now(),
    modified_date TIMESTAMP      NOT NULL DEFAULT now(),
    guid          UUID UNIQUE    NOT NULL,
    account_id    BIGINT         NOT NULL REFERENCES users.account (id) ON DELETE CASCADE,
    token         VARCHAR UNIQUE NOT NULL
);

CREATE SCHEMA orgs;

CREATE TABLE orgs.org
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP   NOT NULL DEFAULT now(),
    modified_date TIMESTAMP   NOT NULL DEFAULT now(),
    guid          UUID UNIQUE NOT NULL,
    name          VARCHAR     NOT NULL
);

CREATE TYPE orgs.FEATURE_TYPE AS ENUM ('HOME', 'FORMS');

CREATE TABLE orgs.feature
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP         NOT NULL DEFAULT now(),
    modified_date TIMESTAMP         NOT NULL DEFAULT now(),
    guid          UUID UNIQUE       NOT NULL,
    org_id        BIGINT            NOT NULL REFERENCES orgs.org (id) ON DELETE CASCADE,
    name          VARCHAR           NOT NULL,
    path          VARCHAR           NOT NULL,
    type          orgs.FEATURE_TYPE NOT NULL,
    UNIQUE (org_id, path)
);

CREATE TABLE orgs.membership
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP NOT NULL DEFAULT now(),
    modified_date TIMESTAMP NOT NULL DEFAULT now(),
    org_id        BIGINT    NOT NULL REFERENCES orgs.org (id) ON DELETE CASCADE,
    account_id    BIGINT    NOT NULL REFERENCES users.account (id) ON DELETE CASCADE,
    UNIQUE (org_id, account_id)
);

CREATE SCHEMA forms;

CREATE TABLE forms.form_template
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP   NOT NULL DEFAULT now(),
    modified_date TIMESTAMP   NOT NULL DEFAULT now(),
    guid          UUID UNIQUE NOT NULL,
    org_id        BIGINT      NOT NULL REFERENCES orgs.org (id) ON DELETE CASCADE,
    title         VARCHAR     NOT NULL,
    description   VARCHAR
);

CREATE TABLE forms.form_template_question
(
    id               BIGSERIAL PRIMARY KEY,
    created_date     TIMESTAMP   NOT NULL DEFAULT now(),
    modified_date    TIMESTAMP   NOT NULL DEFAULT now(),
    guid             UUID UNIQUE NOT NULL,
    form_template_id BIGINT      NOT NULL REFERENCES forms.form_template (id) ON DELETE CASCADE,
    label            VARCHAR     NOT NULL,
    help_text        VARCHAR,
    description      VARCHAR
);
