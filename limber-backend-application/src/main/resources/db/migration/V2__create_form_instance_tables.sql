CREATE TABLE forms.form_instance
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    guid               UUID UNIQUE NOT NULL,
    org_guid           UUID        NOT NULL,
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
    UNIQUE (form_instance_guid, form_template_question_guid)
);
