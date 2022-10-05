select organization.*
from organization.organization
  join organization.organization_hostname on organization_hostname.organization_guid = organization.guid
where organization_hostname.hostname = lower(:hostname)
