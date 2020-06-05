INSERT INTO auth.org_role (guid, created_date, org_guid, name, permissions)
VALUES (:guid, :createdDate, :orgGuid, :name, :permissions::BIT(4))
RETURNING *, (SELECT COUNT(*)
              FROM auth.org_role_membership
              WHERE org_role_membership.org_role_guid = org_role.guid) member_count
