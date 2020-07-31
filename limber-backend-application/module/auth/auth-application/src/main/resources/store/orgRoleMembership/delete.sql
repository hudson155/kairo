DELETE
FROM auth.org_role_membership
WHERE (SELECT org_guid FROM auth.org_role WHERE guid = org_role_guid) = :orgGuid
  AND org_role_guid = :orgRoleGuid
  AND account_guid = :accountGuid
