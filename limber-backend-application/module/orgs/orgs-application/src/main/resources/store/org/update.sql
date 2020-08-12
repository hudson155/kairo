UPDATE orgs.org
SET name = COALESCE(:name, name)
WHERE guid = :orgGuid
