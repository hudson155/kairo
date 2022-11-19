update organization.feature
set name      = :name,
    icon_name = :iconName,
    root_path = lower(:rootPath)
where organization_guid = :organizationGuid
  and guid = :guid
returning *
