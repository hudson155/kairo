ALTER TABLE auth.tenant
    ALTER COLUMN org_guid DROP NOT NULL;

ALTER TABLE auth.tenant
    ALTER COLUMN auth0_client_id DROP NOT NULL;
