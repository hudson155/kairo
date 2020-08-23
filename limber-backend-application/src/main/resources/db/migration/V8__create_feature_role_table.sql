CREATE TABLE auth.feature_role
(
    guid          UUID      NOT NULL,
    created_date  TIMESTAMP NOT NULL,
    feature_guid  UUID      NOT NULL,
    org_role_guid UUID      NOT NULL,
    permissions   VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX uniq__feature_role__guid
    ON auth.feature_role (guid);

CREATE UNIQUE INDEX uniq__feature_role__org_role_guid
    ON auth.feature_role (feature_guid, org_role_guid);

ALTER TABLE auth.feature_role
    ADD CONSTRAINT fk__feature_role__org_role_guid FOREIGN KEY (org_role_guid)
        REFERENCES auth.org_role (guid) ON DELETE CASCADE;
