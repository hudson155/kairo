package limber.exception.organizationAuth

import limber.exception.ConflictException

internal class OrganizationAlreadyHasOrganizationAuth : ConflictException("Organization already has auth.")
