insert into organization.organization_hostname (organization_guid, guid, hostname)
values (:organizationGuid, :guid, lower(:hostname))
returning *
