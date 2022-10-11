delete
from organization.organization_auth
where organization_guid = :organizationGuid
returning *
