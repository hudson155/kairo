SELECT *
FROM auth.feature_role
WHERE feature_guid = :featureGuid
  AND guid = :featureRoleGuid
