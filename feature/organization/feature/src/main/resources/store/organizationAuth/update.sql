update organization.organization_auth
set auth0_organization_id = :auth0OrganizationId
where guid = :guid
returning *
