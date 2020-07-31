UPDATE auth.org_role
SET archived_date = NOW()
WHERE org_guid = :orgGuid
  AND guid = :orgRoleGuid
  AND archived_date IS NULL
