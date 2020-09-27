SELECT *
FROM users.user
WHERE org_guid = :orgGuid
    AND LOWER(email_address) = LOWER(:emailAddress)
