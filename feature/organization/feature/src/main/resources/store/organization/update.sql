update organization.organization
set name = :name
where guid = :guid
returning *
