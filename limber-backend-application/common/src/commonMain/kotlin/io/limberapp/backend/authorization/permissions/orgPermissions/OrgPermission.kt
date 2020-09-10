package io.limberapp.backend.authorization.permissions.orgPermissions

import io.limberapp.backend.authorization.permissions.Permission

@Suppress("MagicNumber")
enum class OrgPermission(
  override val bit: Int,
  override val title: String,
  override val description: String,
) : Permission {
  MANAGE_ORG_ROLES(
    bit = 0,
    title = "Manage organization roles",
    description = "Allows the user to manage the organization's roles and permissions, as well as to manage" +
      " permissions for features.",
  ),
  MANAGE_ORG_ROLE_MEMBERSHIPS(
    bit = 1,
    title = "Manage organization role memberships",
    description = "Allows the user to manage role memberships for all of the organization's members.",
  ),
  MANAGE_ORG_METADATA(
    bit = 2,
    title = "Manage organization metadata",
    description = "Allows the user to manage organization metadata such as the name and photo.",
  ),
  MANAGE_ORG_FEATURES(
    bit = 3,
    title = "Manage organization features",
    description = "Allows the user to create, manage, and delete organization features.",
  ),
  MODIFY_OWN_METADATA(
    bit = 4,
    title = "Change own name",
    description = "Allows the user change their display name.",
  ),
}
