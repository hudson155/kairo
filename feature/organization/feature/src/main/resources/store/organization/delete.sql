delete
from organization.organization
where id = :id
returning *
