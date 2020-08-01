UPDATE users.user
SET identity_provider = COALESCE(:identityProvider, identity_provider),
    superuser         = COALESCE(:superuser, superuser),
    name              = COALESCE(:firstName, first_name) || ' ' || COALESCE(:lastName, last_name),
    first_name        = COALESCE(:firstName, first_name),
    last_name         = COALESCE(:lastName, last_name)
WHERE guid = :userGuid
  AND archived_date IS NULL
