SELECT *
FROM auth.tenant_domain
WHERE org_guid = :orgGuid
  AND LOWER(domain) = LOWER(:domain)
