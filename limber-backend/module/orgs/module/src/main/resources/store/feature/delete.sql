DELETE
FROM orgs.feature
WHERE org_guid = :orgGuid
  AND guid = :featureGuid
