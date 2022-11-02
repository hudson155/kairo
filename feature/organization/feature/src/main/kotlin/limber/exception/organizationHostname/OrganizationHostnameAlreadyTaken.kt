package limber.exception.organizationHostname

import limber.exception.ConflictException

internal class OrganizationHostnameAlreadyTaken : ConflictException("Organization hostname already taken.")
