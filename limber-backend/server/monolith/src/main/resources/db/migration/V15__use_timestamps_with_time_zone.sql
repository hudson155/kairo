ALTER TABLE auth.tenant
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE auth.tenant_domain
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE orgs.org
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE auth.org_role
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE auth.org_role_membership
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE orgs.feature
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE users.account
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE forms.form_template
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE forms.form_template_question
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE forms.form_instance
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE forms.form_instance
    ALTER COLUMN submitted_date TYPE TIMESTAMPTZ
        USING submitted_date AT TIME ZONE 'UTC';

ALTER TABLE forms.form_instance_question
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';

ALTER TABLE auth.feature_role
    ALTER COLUMN created_date TYPE TIMESTAMPTZ
        USING created_date AT TIME ZONE 'UTC';
