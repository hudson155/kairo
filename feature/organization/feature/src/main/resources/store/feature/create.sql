insert into organization.feature (organization_guid, guid, is_default, type, name, icon_name, root_path)
values (:organizationGuid, :guid, :isDefault, :type, :name, :iconName, lower(:rootPath))
returning *
