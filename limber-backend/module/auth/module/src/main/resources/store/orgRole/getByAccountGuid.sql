SELECT *,
       (SELECT COUNT(*)
        FROM auth.org_role_membership
        WHERE org_role_membership.org_role_guid = org_role.guid) member_count
FROM auth.org_role
WHERE org_guid = :orgGuid
  AND (EXISTS(SELECT 1
              FROM auth.org_role_membership
              WHERE org_role_guid = org_role.guid
                AND account_guid = :accountGuid) OR is_default IS TRUE)
