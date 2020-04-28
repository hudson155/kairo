UPDATE orgs.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions::BIT(3), permissions)
WHERE org_guid = :orgGuid
  AND guid = :guid
  AND archived_date IS NULL
