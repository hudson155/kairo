SELECT *,
       (SELECT COUNT(*)
        FROM auth.org_role_membership
        WHERE org_role_membership.org_role_guid = org_role.guid) member_count
FROM auth.org_role
WHERE guid = :guid
  AND archived_date IS NULL
