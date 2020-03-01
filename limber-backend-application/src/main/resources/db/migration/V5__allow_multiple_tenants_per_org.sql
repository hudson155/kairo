ALTER TABLE auth.tenant
    DROP CONSTRAINT tenant_auth0_client_id_key;

ALTER TABLE auth.tenant
    DROP CONSTRAINT tenant_org_guid_key;
