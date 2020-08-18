CREATE TABLE auth.org_role
(
    guid         UUID      NOT NULL,
    created_date TIMESTAMP NOT NULL,
    org_guid     UUID      NOT NULL,
    name         VARCHAR   NOT NULL,
    permissions  VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX uniq__org_role__guid
    ON auth.org_role (guid);

CREATE UNIQUE INDEX uniq__org_role__name
    ON auth.org_role (org_guid, LOWER(name));

CREATE TABLE auth.org_role_membership
(
    created_date  TIMESTAMP NOT NULL,
    org_role_guid UUID      NOT NULL REFERENCES auth.org_role (guid) ON DELETE CASCADE,
    account_guid  UUID      NOT NULL,
    UNIQUE (org_role_guid, account_guid)
);
