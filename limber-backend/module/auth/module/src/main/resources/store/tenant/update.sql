UPDATE auth.tenant
SET auth0_org_id = COALESCE(:auth0OrgId, auth0_org_id)
WHERE org_guid = :orgGuid
RETURNING *
