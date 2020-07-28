INSERT INTO orgs.org (guid, created_date, name, owner_account_guid)
VALUES (:guid, :createdDate, :name, :ownerAccountGuid)
RETURNING *
