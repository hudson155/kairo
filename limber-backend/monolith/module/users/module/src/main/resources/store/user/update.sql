UPDATE users.user
SET identity_provider = COALESCE(:identityProvider, identity_provider),
    superuser         = COALESCE(:superuser, superuser),
    first_name        = COALESCE(:firstName, first_name),
    last_name         = COALESCE(:lastName, last_name)
WHERE guid = :userGuid
RETURNING *
