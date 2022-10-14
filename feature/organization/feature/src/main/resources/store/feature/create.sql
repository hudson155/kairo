insert into organization.feature (organization_guid, guid, is_default, type, root_path)
values (:organizationGuid, :guid, :isDefault, :type, lower(:rootPath))
returning *
