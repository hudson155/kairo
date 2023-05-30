insert into organization.organization_hostname (id, organization_id, hostname)
values (:id, :organizationId, lower(:hostname))
returning *
