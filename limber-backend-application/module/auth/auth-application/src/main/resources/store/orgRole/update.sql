UPDATE auth.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions, permissions)
WHERE guid = :guid
  AND archived_date IS NULL
