SELECT *
FROM orgs.org
WHERE guid = :orgGuid
  AND archived_date IS NULL
