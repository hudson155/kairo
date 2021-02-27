CREATE TABLE orgs.feature
(
    guid               UUID        NOT NULL,
    created_date       TIMESTAMPTZ NOT NULL,
    org_guid           UUID        NOT NULL,
    name               VARCHAR     NOT NULL,
    path               VARCHAR     NOT NULL,
    type               VARCHAR     NOT NULL,
    rank               INT         NOT NULL,
    is_default_feature BOOLEAN     NOT NULL
);

CREATE UNIQUE INDEX uniq__feature__guid
    ON orgs.feature (guid);

CREATE INDEX idx__feature__org_guid
    ON orgs.feature (org_guid);

ALTER TABLE orgs.feature
    ADD CONSTRAINT fk__feature__org_guid FOREIGN KEY (org_guid)
        REFERENCES orgs.org (guid) ON DELETE CASCADE;

CREATE UNIQUE INDEX uniq__feature__name
    ON orgs.feature (org_guid, LOWER(name));

CREATE UNIQUE INDEX uniq__feature__path
    ON orgs.feature (org_guid, LOWER(path));

CREATE UNIQUE INDEX uniq__feature__rank
    ON orgs.feature (org_guid, rank);

CREATE UNIQUE INDEX uniq__feature__is_default_feature ON orgs.feature (org_guid)
    WHERE is_default_feature IS TRUE;
