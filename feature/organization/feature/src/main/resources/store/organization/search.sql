select organization.*
from organization.organization
  join organization.organization_auth
    on organization_auth.organization_guid = organization.guid
  join organization.organization_hostname
    on organization_hostname.organization_guid = organization.guid
where lower(organization.name) like ('%' || lower(:search) || '%')
   or lower(organization_auth.auth0_organization_id) like ('%' || lower(:search) || '%')
   or lower(organization_hostname.hostname) like ('%' || lower(:search) || '%')
