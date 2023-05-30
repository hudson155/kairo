insert into organization.organization_auth (id, organization_id, auth0_organization_id, auth0_organization_name)
values (:id, :organizationId, null, lower(:auth0OrganizationName))
returning *
