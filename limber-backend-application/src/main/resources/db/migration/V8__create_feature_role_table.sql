CREATE TABLE auth.feature_role
(
    guid          UUID UNIQUE NOT NULL,
    created_date  TIMESTAMP   NOT NULL,
    feature_guid  UUID        NOT NULL,
    org_role_guid UUID        NOT NULL REFERENCES auth.org_role (guid) ON DELETE CASCADE,
    permissions   VARCHAR     NOT NULL,
    UNIQUE (feature_guid, org_role_guid)
);
