INSERT INTO auth.org_role (guid, created_date, org_guid, name, permissions)
VALUES (:guid, :createdDate, :orgGuid, :name, :permissions::BIT(3))
