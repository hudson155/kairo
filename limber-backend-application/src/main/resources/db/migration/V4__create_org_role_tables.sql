CREATE TABLE auth.org_role
(
    id            BIGSERIAL PRIMARY KEY,
    guid          UUID UNIQUE NOT NULL,
    created_date  TIMESTAMP   NOT NULL,
    archived_date TIMESTAMP DEFAULT NULL,
    org_guid      UUID        NOT NULL,
    name          VARCHAR     NOT NULL,
    permissions   BIT(3)      NOT NULL
);

CREATE UNIQUE INDEX ON auth.org_role (org_guid, LOWER(name));
