SELECT org_role.*,
       (SELECT COUNT(*)
        FROM auth.org_role_membership
        WHERE org_role_membership.org_role_guid = org_role.guid) member_count
FROM auth.org_role
WHERE org_guid = :orgGuid
  AND EXISTS(SELECT id
             FROM auth.org_role_membership
             WHERE org_role_guid = org_role.guid
               AND account_guid = :accountGuid)
  AND archived_date IS NULL
