UPDATE auth.feature_role
SET permissions = COALESCE(:permissions, permissions)
WHERE guid = :guid
