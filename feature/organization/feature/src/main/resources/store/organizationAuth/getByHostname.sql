select organization_auth.*
from organization.organization_auth
  join organization.organization_hostname
    on organization_hostname.organization_guid = organization_auth.organization_guid
where organization_hostname.hostname = lower(:hostname)
