SELECT *
FROM auth.tenant
WHERE EXISTS(SELECT id
             FROM auth.tenant_domain
             WHERE tenant_domain.org_guid = tenant.org_guid
               AND LOWER(domain) = LOWER(:domain)
    )
  AND archived_date IS NULL
