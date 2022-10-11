update organization.feature
set root_path = lower(:rootPath)
where organization_guid = :organizationGuid
  and guid = :guid
returning *
