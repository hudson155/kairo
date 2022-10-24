insert into organization.feature (organization_guid, guid, is_default, type, name, root_path)
values (:organizationGuid, :guid, :isDefault, :type, :name, lower(:rootPath))
returning *
