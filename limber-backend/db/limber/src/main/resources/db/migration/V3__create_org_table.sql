CREATE TABLE orgs.org
(
    guid            UUID        NOT NULL,
    created_date    TIMESTAMPTZ NOT NULL,
    name            VARCHAR     NOT NULL,
    owner_user_guid UUID
);

CREATE UNIQUE INDEX uniq__org__guid
    ON orgs.org (guid);

CREATE UNIQUE INDEX uniq__org__owner_user_guid
    ON orgs.org (owner_user_guid)
    WHERE owner_user_guid IS NOT NULL;
