UPDATE users.user
SET archived_date = NOW()
WHERE guid = :userGuid
  AND archived_date IS NULL
