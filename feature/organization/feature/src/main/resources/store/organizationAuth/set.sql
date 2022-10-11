insert into organization.organization_auth (organization_guid, guid, auth0_organization_id)
values (:organizationGuid, :guid, :auth0OrganizationId)
on conflict (organization_guid) do update
  set auth0_organization_id = :auth0OrganizationId
returning *
