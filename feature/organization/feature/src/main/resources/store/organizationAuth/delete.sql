delete
from organization.organization_auth
where id = :id
returning *
