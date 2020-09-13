UPDATE auth.feature_role
SET permissions = COALESCE(:permissions, permissions),
    is_default  = COALESCE(:isDefault, is_default)
WHERE feature_guid = :featureGuid
  AND guid = :featureRoleGuid
RETURNING *
