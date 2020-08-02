UPDATE orgs.org
SET archived_date = NOW()
WHERE guid = :orgGuid
  AND archived_date IS NULL
