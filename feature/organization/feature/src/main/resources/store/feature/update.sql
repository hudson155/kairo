update organization.feature
set name      = :name,
    root_path = lower(:rootPath)
where organization_guid = :organizationGuid
  and guid = :guid
returning *
