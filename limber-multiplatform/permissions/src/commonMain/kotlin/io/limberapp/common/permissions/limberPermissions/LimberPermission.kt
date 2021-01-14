package io.limberapp.common.permissions.limberPermissions

import io.limberapp.common.permissions.Permission

@Suppress("MagicNumber")
enum class LimberPermission(
    override val index: Int,
    override val title: String,
    override val description: String,
) : Permission {
  IDENTITY_PROVIDER(
      index = 0,
      title = "Identity provider",
      description = "Grants access to for all accounts' metadata, specifically that which is" +
          " necessary to issue JWTs.",
  ),
  ORG_ACCESS_OVERRIDE(
      index = 1,
      title = "Org access override",
      description = "Grants access to all orgs, including all org permissions and feature" +
          " permissions. Does not grant any access defined by other Limber permissions.",
  );
}
