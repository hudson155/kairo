UPDATE auth.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions, permissions)
WHERE org_guid = :orgGuid
  AND guid = :orgRoleGuid
