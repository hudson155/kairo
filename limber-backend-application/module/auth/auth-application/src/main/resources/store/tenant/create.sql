INSERT INTO auth.tenant (created_date, org_guid, auth0_client_id)
VALUES (:createdDate, :orgGuid, :auth0ClientId)
RETURNING *
