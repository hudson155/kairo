CREATE SCHEMA forms;

CREATE TABLE forms.form_template
(
    id           BIGSERIAL PRIMARY KEY,
    guid         UUID UNIQUE NOT NULL,
    created_date TIMESTAMP   NOT NULL,
    feature_guid UUID        NOT NULL,
    title        VARCHAR     NOT NULL,
    description  VARCHAR
);

CREATE TABLE forms.form_template_question
(
    id                 BIGSERIAL PRIMARY KEY,
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    form_template_guid UUID        NOT NULL REFERENCES forms.form_template (guid) ON DELETE CASCADE,
    rank               INT         NOT NULL,
    label              VARCHAR     NOT NULL,
    help_text          VARCHAR,
    type               VARCHAR     NOT NULL,
    multi_line         BOOLEAN,
    placeholder        VARCHAR,
    validator          VARCHAR,
    earliest           DATE,
    latest             DATE,
    options            TEXT[]
);

CREATE TABLE forms.form_instance
(
    id                 BIGSERIAL PRIMARY KEY,
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    feature_guid       UUID        NOT NULL,
    form_template_guid UUID        NOT NULL REFERENCES forms.form_template (guid) ON DELETE RESTRICT
);

CREATE TABLE forms.form_instance_question
(
    id                          BIGSERIAL PRIMARY KEY,
    created_date                TIMESTAMP NOT NULL,
    form_instance_guid          UUID      NOT NULL REFERENCES forms.form_instance (guid) ON DELETE CASCADE,
    form_template_question_guid UUID      REFERENCES forms.form_template_question (guid) ON DELETE SET NULL,
    type                        VARCHAR   NOT NULL,
    text                        VARCHAR,
    date                        DATE,
    selections                  TEXT[],
    UNIQUE (form_instance_guid, form_template_question_guid)
);
