update organization.organization_auth
set auth0_organization_id   = :auth0OrganizationId,
    auth0_organization_name = lower(:auth0OrganizationName)
where id = :id
returning *
