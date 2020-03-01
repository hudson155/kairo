ALTER TABLE users.user
    ADD COLUMN org_guid UUID;

UPDATE users.user
SET org_guid = (SELECT org_guid FROM orgs.membership WHERE orgs.membership.account_guid = users.user.account_guid);

ALTER TABLE users.user
    ALTER COLUMN org_guid SET NOT NULL;

DROP TABLE orgs.membership;
