delete
from organization.feature
where organization_guid = :organizationGuid
  and guid = :guid
returning *
