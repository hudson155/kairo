UPDATE orgs.org
SET name = COALESCE(:name, name)
WHERE guid = :guid
  AND archived_date IS NULL
