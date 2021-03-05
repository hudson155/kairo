CREATE TABLE auth.org_role
(
    guid         UUID        NOT NULL,
    created_date TIMESTAMPTZ NOT NULL,
    org_guid     UUID        NOT NULL,
    name         VARCHAR     NOT NULL,
    permissions  VARCHAR     NOT NULL,
    is_default   BOOLEAN     NOT NULL
);

CREATE UNIQUE INDEX uniq__org_role__guid
    ON auth.org_role (guid);

CREATE UNIQUE INDEX uniq__org_role__name
    ON auth.org_role (org_guid, LOWER(name));

CREATE TABLE auth.org_role_membership
(
    created_date  TIMESTAMPTZ NOT NULL,
    org_role_guid UUID        NOT NULL,
    user_guid     UUID        NOT NULL
);

CREATE INDEX idx__org_role_membership__org_role_guid
    ON auth.org_role_membership (org_role_guid);

ALTER TABLE auth.org_role_membership
    ADD CONSTRAINT fk__org_role_membership__org_role_guid FOREIGN KEY (org_role_guid)
        REFERENCES auth.org_role (guid) ON DELETE CASCADE;

CREATE UNIQUE INDEX uniq__org_role_membership__user_guid
    ON auth.org_role_membership (org_role_guid, user_guid);