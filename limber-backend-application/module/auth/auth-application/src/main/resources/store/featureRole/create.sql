INSERT INTO auth.feature_role (guid, created_date, feature_guid, org_role_guid, permissions, is_default)
VALUES (:guid, :createdDate, :featureGuid, :orgRoleGuid, :permissions, :isDefault)
RETURNING *
