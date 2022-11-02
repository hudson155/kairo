update organization.feature
set is_default = (guid = :guid)
where organization_guid = (select organization_guid
                           from organization.feature
                           where guid = :guid)
returning *
