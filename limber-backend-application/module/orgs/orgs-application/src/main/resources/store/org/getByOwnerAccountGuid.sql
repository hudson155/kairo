SELECT *
FROM orgs.org
WHERE owner_account_guid = :ownerAccountGuid
  AND archived_date IS NULL
