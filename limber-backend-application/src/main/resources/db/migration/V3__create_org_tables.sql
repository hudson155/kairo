CREATE TABLE orgs.org
(
    guid               UUID UNIQUE NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    name               VARCHAR     NOT NULL,
    owner_account_guid UUID        NOT NULL
);
