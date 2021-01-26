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
  SUPERUSER(
      index = 1,
      title = "Superuser",
      description = "Overrides user, org, and feature checks. Users with this permission can" +
          " essentially impersonate any other user, but don't implicitly receive any other Limber" +
          " permissions.",
  );
}
