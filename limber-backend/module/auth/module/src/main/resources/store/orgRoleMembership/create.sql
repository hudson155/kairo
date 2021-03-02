INSERT INTO auth.org_role_membership (created_date, org_role_guid, user_guid)
VALUES (:createdDate, :orgRoleGuid, :userGuid)
RETURNING *
