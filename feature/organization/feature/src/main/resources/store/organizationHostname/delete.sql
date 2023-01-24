delete
from organization.organization_hostname
where guid = :guid
returning *
