insert into organization.feature (id, organization_id, is_default, type, name, icon_name, root_path)
values (:id, :organizationId, :isDefault, :type, :name, :iconName, lower(:rootPath))
returning *
