UPDATE orgs.feature
SET archived_date = NOW()
WHERE org_guid = :orgGuid
  AND guid = :featureGuid
  AND archived_date IS NULL
