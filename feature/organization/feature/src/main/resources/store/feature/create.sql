insert into organization.feature (guid, organization_guid, is_default, type, root_path)
values (:guid, :organizationGuid, :isDefault, :type, lower(:rootPath))
returning *
