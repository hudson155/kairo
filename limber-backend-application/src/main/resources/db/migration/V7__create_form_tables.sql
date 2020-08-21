CREATE SCHEMA forms;

CREATE TABLE forms.form_template
(
    guid         UUID      NOT NULL,
    created_date TIMESTAMP NOT NULL,
    feature_guid UUID      NOT NULL,
    title        VARCHAR   NOT NULL,
    description  VARCHAR
);

CREATE UNIQUE INDEX uniq__form_template__guid
    ON forms.form_template (guid);

CREATE UNIQUE INDEX uniq__form_template__feature_guid__1
    ON forms.form_template (feature_guid, guid);

CREATE TABLE forms.form_template_question
(
    guid               UUID      NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    form_template_guid UUID      NOT NULL,
    rank               INT       NOT NULL,
    label              VARCHAR   NOT NULL,
    help_text          VARCHAR,
    required           BOOLEAN   NOT NULL,
    type               VARCHAR   NOT NULL,
    multi_line         BOOLEAN,
    placeholder        VARCHAR,
    validator          VARCHAR,
    earliest           DATE,
    latest             DATE,
    options            TEXT[]
);

CREATE UNIQUE INDEX uniq__form_template_question__guid
    ON forms.form_template_question (guid);

ALTER TABLE forms.form_template_question
    ADD CONSTRAINT fk__form_template__form_template_guid FOREIGN KEY (form_template_guid)
        REFERENCES forms.form_template (guid) ON DELETE CASCADE;

ALTER TABLE forms.form_template_question
    ADD CONSTRAINT uniq__form_template__rank UNIQUE (form_template_guid, rank)
        DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE forms.form_instance
(
    guid                 UUID UNIQUE NOT NULL,
    created_date         TIMESTAMP   NOT NULL,
    feature_guid         UUID        NOT NULL,
    form_template_guid   UUID        NOT NULL,
    number               BIGINT      NOT NULL,
    submitted_date       TIMESTAMP,
    creator_account_guid UUID        NOT NULL,
    CONSTRAINT fk__form_instance__form_template_guid FOREIGN KEY (feature_guid, form_template_guid)
        REFERENCES forms.form_template (feature_guid, guid) ON DELETE RESTRICT
);

CREATE TABLE forms.form_instance_question
(
    created_date       TIMESTAMP NOT NULL,
    form_instance_guid UUID      NOT NULL REFERENCES forms.form_instance (guid) ON DELETE CASCADE,
    question_guid      UUID      REFERENCES forms.form_template_question (guid) ON DELETE SET NULL,
    type               VARCHAR   NOT NULL,
    text               VARCHAR,
    date               DATE,
    selections         TEXT[],
    UNIQUE (form_instance_guid, question_guid)
);
