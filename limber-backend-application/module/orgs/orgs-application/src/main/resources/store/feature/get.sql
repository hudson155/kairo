SELECT *
FROM orgs.feature
WHERE org_guid = :orgGuid
  AND guid = :featureGuid
  AND archived_date IS NULL
ORDER BY rank
