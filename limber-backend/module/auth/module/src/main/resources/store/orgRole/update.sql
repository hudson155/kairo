UPDATE auth.org_role
SET name        = COALESCE(:name, name),
    permissions = COALESCE(:permissions, permissions),
    is_default  = COALESCE(:isDefault, is_default)
WHERE guid = :orgRoleGuid
RETURNING *, (SELECT COUNT(*)
              FROM auth.org_role_membership
              WHERE org_role_membership.org_role_guid = org_role.guid) member_count
