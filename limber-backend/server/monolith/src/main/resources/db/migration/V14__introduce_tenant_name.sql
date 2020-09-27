ALTER TABLE auth.tenant
    ADD COLUMN name TEXT;

UPDATE auth.tenant
SET name = (SELECT name FROM orgs.org WHERE guid = org_guid);

ALTER TABLE auth.tenant
    ALTER COLUMN name
        SET NOT NULL;
