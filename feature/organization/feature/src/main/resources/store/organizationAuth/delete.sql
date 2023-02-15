delete
from organization.organization_auth
where guid = :authGuid
returning *
