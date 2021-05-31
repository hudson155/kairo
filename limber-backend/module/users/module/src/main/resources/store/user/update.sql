UPDATE users.user
SET permissions = COALESCE(:permissions, permissions),
    full_name   = COALESCE(:fullName, full_name)
WHERE guid = :userGuid
RETURNING *
