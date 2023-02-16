package limber.exception.organizationAuth

import limber.exception.ConflictException

internal class Auth0OrganizationNameAlreadyTaken : ConflictException("Auth0 organization name already taken.")
