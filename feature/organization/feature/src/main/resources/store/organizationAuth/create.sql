insert into organization.organization_auth (guid, organization_guid, auth0_organization_id)
values (:guid, :organizationGuid, :auth0OrganizationId)
returning *
