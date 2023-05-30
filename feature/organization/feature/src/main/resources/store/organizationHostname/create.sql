insert into organization.organization_hostname (id, organization_guid, hostname)
values (:id, :organizationGuid, lower(:hostname))
returning *
