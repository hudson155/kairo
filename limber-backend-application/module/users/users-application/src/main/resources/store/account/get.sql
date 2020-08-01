SELECT *
FROM users.account
WHERE guid = :accountGuid
  AND archived_date IS NULL
