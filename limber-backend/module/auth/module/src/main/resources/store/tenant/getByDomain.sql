SELECT tenant.*
FROM auth.tenant
WHERE EXISTS(SELECT 1
             FROM auth.tenant_domain
             WHERE tenant_domain.org_guid = tenant.org_guid
               AND LOWER(tenant_domain.domain) = LOWER(:domain))
