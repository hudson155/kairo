CREATE TABLE orgs.org
(
    id                 BIGSERIAL PRIMARY KEY,
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    name               VARCHAR     NOT NULL,
    owner_account_guid UUID        NOT NULL
);

CREATE TABLE orgs.feature
(
    id                 BIGSERIAL PRIMARY KEY,
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    org_guid           UUID        NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    name               VARCHAR     NOT NULL,
    path               VARCHAR     NOT NULL,
    type               VARCHAR     NOT NULL,
    is_default_feature BOOLEAN     NOT NULL
);

CREATE UNIQUE INDEX
    ON orgs.feature (org_guid, LOWER(path));

CREATE UNIQUE INDEX
    ON orgs.feature (org_guid)
    WHERE is_default_feature IS TRUE;
