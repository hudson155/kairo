SELECT *
FROM users.user
WHERE guid = :userGuid
  AND archived_date IS NULL
