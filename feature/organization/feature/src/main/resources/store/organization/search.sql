select organization.*
from organization.organization
  join organization.organization_auth
    on organization_auth.organization_id = organization.id
  join organization.organization_hostname
    on organization_hostname.organization_id = organization.id
where lower(organization.name) like ('%' || lower(:search) || '%')
   or lower(organization_auth.auth0_organization_id) like ('%' || lower(:search) || '%')
   or lower(organization_hostname.hostname) like ('%' || lower(:search) || '%')
