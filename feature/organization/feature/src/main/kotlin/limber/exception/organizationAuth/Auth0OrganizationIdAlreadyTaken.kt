package limber.exception.organizationAuth

import limber.exception.ConflictException

internal class Auth0OrganizationIdAlreadyTaken : ConflictException("Auth0 organization ID already taken.")
