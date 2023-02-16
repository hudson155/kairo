insert into organization.organization_auth (guid, organization_guid, auth0_organization_id, auth0_organization_name)
values (:guid, :organizationGuid, null, lower(:auth0OrganizationName))
returning *
