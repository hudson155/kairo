package io.limberapp.common.permissions.orgPermissions

import io.limberapp.common.permissions.Permission

@Suppress("MagicNumber")
enum class OrgPermission(
    override val index: Int,
    override val title: String,
    override val description: String,
) : Permission {
  MANAGE_ORG_ROLES(
      index = 0,
      title = "Manage roles",
      description = "Manage the organization's roles and permissions, as well as manage" +
          " permissions for features.",
  ),
  MANAGE_ORG_ROLE_MEMBERSHIPS(
      index = 1,
      title = "Manage role memberships",
      description = "Manage role memberships for all of the organization's members.",
  ),
  MANAGE_ORG_METADATA(
      index = 2,
      title = "Manage organization",
      description = "Manage organization metadata such as the name and photo.",
  ),
  MODIFY_OWN_METADATA(
      index = 3,
      title = "Change own name",
      description = "Change one's own display name.",
  );
}
