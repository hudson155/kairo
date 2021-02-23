UPDATE users.user
SET permissions = COALESCE(:permissions, permissions),
    first_name  = COALESCE(:firstName, first_name),
    last_name   = COALESCE(:lastName, last_name)
WHERE guid = :userGuid
RETURNING *
