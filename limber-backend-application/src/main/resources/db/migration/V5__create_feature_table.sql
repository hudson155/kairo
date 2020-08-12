CREATE TABLE orgs.feature
(
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    org_guid           UUID        NOT NULL REFERENCES orgs.org (guid) ON DELETE CASCADE,
    rank               INT         NOT NULL,
    name               VARCHAR     NOT NULL,
    path               VARCHAR     NOT NULL,
    type               VARCHAR     NOT NULL,
    is_default_feature BOOLEAN     NOT NULL,
    UNIQUE (org_guid, rank)
);

CREATE UNIQUE INDEX ON orgs.feature (org_guid, LOWER(path));

CREATE UNIQUE INDEX ON orgs.feature (org_guid) WHERE is_default_feature IS TRUE;
