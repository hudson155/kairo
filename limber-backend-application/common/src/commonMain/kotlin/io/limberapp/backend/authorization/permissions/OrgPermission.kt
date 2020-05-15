package io.limberapp.backend.authorization.permissions

@Suppress("MagicNumber")
enum class OrgPermission(val bit: Int, val title: String, val description: String) {
  MANAGE_ORG_ROLES(
    bit = 0,
    title = "Manage organization roles",
    description = "Allows the user to manage the organization's roles."
  ),
  MANAGE_ORG_ROLE_MEMBERSHIPS(
    bit = 1,
    title = "Manage organization role memberships",
    description = "Allows the user to manage role memberships for all of the organization's members."
  ),
  MANAGE_ORG_METADATA(
    bit = 2,
    title = "Manage organization metadata",
    description = "Allows the user to manage organization metadata such as the name and photo."
  ),
  MANAGE_ORG_FEATURES(
    bit = 3,
    title = "Manage organization features",
    description = "Allows the user to create, manage, and delete organization features."
  ),
}
