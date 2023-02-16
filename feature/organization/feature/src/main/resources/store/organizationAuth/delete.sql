delete
from organization.organization_auth
where guid = :guid
returning *
