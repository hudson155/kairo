UPDATE auth.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions::BIT(4), permissions)
WHERE guid = :guid
  AND archived_date IS NULL
