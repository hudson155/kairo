SELECT *
FROM orgs.feature
WHERE guid = :orgGuid
  AND archived_date IS NULL
ORDER BY rank
