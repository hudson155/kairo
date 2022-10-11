update organization.feature
set is_default = (guid = :guid)
where organization_guid = :organizationGuid
returning *
