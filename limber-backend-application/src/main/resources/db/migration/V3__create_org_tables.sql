CREATE TABLE orgs.org
(
    id                 BIGSERIAL PRIMARY KEY,
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    archived_date      TIMESTAMP DEFAULT NULL,
    name               VARCHAR     NOT NULL,
    owner_account_guid UUID        NOT NULL
);
