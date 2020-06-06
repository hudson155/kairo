CREATE TABLE auth.org_role
(
    id            BIGSERIAL PRIMARY KEY,
    guid          UUID UNIQUE NOT NULL,
    created_date  TIMESTAMP   NOT NULL,
    archived_date TIMESTAMP DEFAULT NULL,
    org_guid      UUID        NOT NULL,
    name          VARCHAR     NOT NULL,
    permissions   BIT(4)      NOT NULL
);

CREATE UNIQUE INDEX ON auth.org_role (org_guid, LOWER(name)) WHERE archived_date IS NULL;

CREATE TABLE auth.org_role_membership
(
    id            BIGSERIAL PRIMARY KEY,
    created_date  TIMESTAMP NOT NULL,
    org_role_guid UUID      NOT NULL REFERENCES auth.org_role (guid) ON DELETE CASCADE,
    account_guid  UUID      NOT NULL,
    UNIQUE (org_role_guid, account_guid)
);
