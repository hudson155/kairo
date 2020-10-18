SELECT *
FROM orgs.feature
WHERE org_guid = :orgGuid
ORDER BY rank
