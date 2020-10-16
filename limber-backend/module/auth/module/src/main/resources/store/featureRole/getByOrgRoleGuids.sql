SELECT *
FROM auth.feature_role
WHERE feature_guid = :featureGuid
  AND org_role_guid = ANY (:orgRoleGuids)
