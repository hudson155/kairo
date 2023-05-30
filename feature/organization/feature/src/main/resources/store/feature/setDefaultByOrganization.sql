update organization.feature
set is_default = (id = :id)
where organization_guid = (select organization_guid
                           from organization.feature
                           where id = :id)
returning *
