UPDATE orgs.org
SET name               = COALESCE(:name, name),
    owner_account_guid = COALESCE(:ownerAccountGuid, owner_account_guid)
WHERE guid = :orgGuid
