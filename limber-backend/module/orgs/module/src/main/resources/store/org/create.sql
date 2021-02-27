INSERT INTO orgs.org (guid, created_date, name, owner_user_guid)
VALUES (:guid, :createdDate, :name, :ownerUserGuid)
RETURNING *
