insert into organization.organization_auth (id, organization_guid, auth0_organization_id, auth0_organization_name)
values (:id, :organizationGuid, null, lower(:auth0OrganizationName))
returning *
