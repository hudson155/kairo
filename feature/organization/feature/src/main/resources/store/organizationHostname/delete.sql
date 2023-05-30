delete
from organization.organization_hostname
where id = :id
returning *
