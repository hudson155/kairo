package limber.exception.organizationAuth

internal class OrganizationAuthIdIsNull : Exception(
  "The Auth0 organization ID on the organization auth was null." +
    " It should only be null during the creation process.",
)
