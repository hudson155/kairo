select organization_auth.*
from organization.organization_auth
  join organization.organization_hostname
    on organization_hostname.organization_id = organization_auth.organization_id
where organization_hostname.hostname = lower(:hostname)
