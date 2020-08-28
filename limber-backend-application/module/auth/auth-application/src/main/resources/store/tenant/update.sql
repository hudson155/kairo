UPDATE auth.tenant
SET auth0_client_id = COALESCE(:auth0ClientId, auth0_client_id)
WHERE org_guid = :orgGuid
RETURNING *
