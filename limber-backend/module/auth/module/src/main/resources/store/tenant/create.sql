INSERT INTO auth.tenant (created_date, org_guid, name, auth0_client_id)
VALUES (:createdDate, :orgGuid, :name, :auth0ClientId)
RETURNING *
