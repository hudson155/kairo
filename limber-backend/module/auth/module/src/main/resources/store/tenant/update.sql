UPDATE auth.tenant
SET name            = COALESCE(:name, name),
    auth0_client_id = COALESCE(:auth0ClientId, auth0_client_id)
WHERE org_guid = :orgGuid
RETURNING *
