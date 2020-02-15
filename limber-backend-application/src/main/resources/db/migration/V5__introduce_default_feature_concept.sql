ALTER TABLE orgs.feature
    ADD COLUMN is_default_feature BOOLEAN;

UPDATE orgs.feature
SET is_default_feature = CASE WHEN name = 'Home' THEN TRUE ELSE FALSE END;

ALTER TABLE orgs.feature
    ALTER COLUMN is_default_feature SET NOT NULL;

CREATE UNIQUE INDEX
    ON orgs.feature (org_guid)
    WHERE is_default_feature IS TRUE;
