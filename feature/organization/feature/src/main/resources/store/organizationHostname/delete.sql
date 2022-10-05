delete
from organization.organization_hostname
where organization_guid = :organizationGuid
  and guid = :guid
returning *
