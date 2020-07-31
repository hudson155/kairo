SELECT *
FROM auth.tenant
WHERE auth0_client_id = :auth0ClientId
  AND archived_date IS NULL
