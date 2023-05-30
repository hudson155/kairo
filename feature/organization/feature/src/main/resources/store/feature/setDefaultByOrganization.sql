update organization.feature
set is_default = (id = :id)
where organization_id = (select organization_id
                         from organization.feature
                         where id = :id)
returning *
