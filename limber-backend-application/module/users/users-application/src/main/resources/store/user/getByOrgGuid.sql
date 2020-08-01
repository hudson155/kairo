SELECT *
FROM users.user
WHERE org_guid = :orgGuid
  AND archived_date IS NULL
