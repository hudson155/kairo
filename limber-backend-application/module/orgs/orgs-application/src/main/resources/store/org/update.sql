UPDATE orgs.org
SET name               = COALESCE(:name, name),
    owner_user_guid = COALESCE(:ownerUserGuid, owner_user_guid)
WHERE guid = :orgGuid
RETURNING *
