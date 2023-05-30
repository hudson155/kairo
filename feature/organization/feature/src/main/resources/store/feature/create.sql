insert into organization.feature (id, organization_guid, is_default, type, name, icon_name, root_path)
values (:id, :organizationGuid, :isDefault, :type, :name, :iconName, lower(:rootPath))
returning *
