insert into organization.organization_hostname (guid, organization_guid, hostname)
values (:guid, :organizationGuid, lower(:hostname))
returning *
