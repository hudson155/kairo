SELECT org_role.*,
       (SELECT COUNT(*)
        FROM auth.org_role_membership
        WHERE org_role_membership.org_role_guid = org_role.guid) member_count
FROM auth.org_role
         JOIN auth.org_role_membership ON org_role.guid = org_role_membership.org_role_guid
WHERE org_role.org_guid = :orgGuid
  AND org_role.archived_date IS NULL
  AND org_role_membership.account_guid = :accountGuid
