INSERT INTO auth.tenant (created_date, org_guid, auth0_org_id)
VALUES (:createdDate, :orgGuid, :auth0OrgId)
RETURNING *
