insert into organization.feature (guid, organization_guid, is_default, type, name, icon_name, root_path)
values (:guid, :organizationGuid, :isDefault, :type, :name, :iconName, lower(:rootPath))
returning *
