SELECT tenant.*
FROM auth.tenant
         JOIN auth.tenant_domain ON tenant.org_guid = tenant_domain.org_guid
WHERE LOWER(domain) = LOWER(:domain)
