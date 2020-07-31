SELECT *
FROM auth.tenant
WHERE org_guid = :orgGuid
  AND archived_date IS NULL
