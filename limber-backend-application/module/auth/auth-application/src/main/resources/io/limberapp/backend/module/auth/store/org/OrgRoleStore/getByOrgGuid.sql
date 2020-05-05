SELECT org_role.*,
       (SELECT COUNT(*)
        FROM auth.org_role_membership
        WHERE org_role_membership.org_role_guid = org_role.guid) member_count
FROM auth.org_role
WHERE org_guid = :orgGuid
  AND archived_date IS NULL
