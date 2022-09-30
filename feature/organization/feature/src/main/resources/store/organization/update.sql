update organization.organization
set name = coalesce(:name, name)
where guid = :organizationGuid
returning *
