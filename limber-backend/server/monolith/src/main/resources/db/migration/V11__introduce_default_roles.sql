ALTER TABLE auth.org_role
    ADD COLUMN is_default BOOLEAN;

UPDATE auth.org_role
SET is_default = FALSE
WHERE is_default IS NULL;

ALTER TABLE auth.org_role
    ALTER COLUMN is_default
        SET NOT NULL;

ALTER TABLE auth.feature_role
    ADD COLUMN is_default BOOLEAN;

UPDATE auth.feature_role
SET is_default = FALSE
WHERE is_default IS NULL;

ALTER TABLE auth.feature_role
    ALTER COLUMN is_default
        SET NOT NULL;
