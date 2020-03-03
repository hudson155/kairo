CREATE TABLE orgs.org
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP   NOT NULL,
    guid         UUID UNIQUE NOT NULL,
    name         VARCHAR     NOT NULL
);

CREATE TABLE orgs.feature
(
    id                 BIGSERIAL PRIMARY KEY,
    created_date       TIMESTAMP   NOT NULL,
    guid               UUID UNIQUE NOT NULL,
    org_guid           UUID        NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    name               VARCHAR     NOT NULL,
    path               VARCHAR     NOT NULL,
    type               VARCHAR     NOT NULL,
    is_default_feature BOOLEAN     NOT NULL,
    UNIQUE (org_guid, path)
);

CREATE UNIQUE INDEX
    ON orgs.feature (org_guid)
    WHERE is_default_feature IS TRUE;
