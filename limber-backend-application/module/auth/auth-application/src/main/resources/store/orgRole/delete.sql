DELETE
FROM auth.org_role
WHERE org_guid = :orgGuid
  AND guid = :orgRoleGuid
