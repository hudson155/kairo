UPDATE auth.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions, permissions)
WHERE org_guid = :orgGuid
  AND guid = :orgRoleGuid
RETURNING *, (SELECT COUNT(*)
              FROM auth.org_role_membership
              WHERE org_role_membership.org_role_guid = org_role.guid) member_count
