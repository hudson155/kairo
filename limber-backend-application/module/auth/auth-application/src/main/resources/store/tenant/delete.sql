UPDATE auth.tenant
SET archived_date = NOW()
WHERE org_guid = :orgGuid
  AND archived_date IS NULL
