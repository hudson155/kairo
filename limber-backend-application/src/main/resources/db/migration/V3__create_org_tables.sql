CREATE TABLE orgs.org
(
    guid            UUID      NOT NULL,
    created_date    TIMESTAMP NOT NULL,
    name            VARCHAR   NOT NULL,
    owner_user_guid UUID
);

CREATE UNIQUE INDEX uniq__org__guid
    ON orgs.org (guid);
