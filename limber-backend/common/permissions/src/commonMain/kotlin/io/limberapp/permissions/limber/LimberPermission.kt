package io.limberapp.permissions.limber

import io.limberapp.permissions.Permission

@Suppress("MagicNumber")
enum class LimberPermission(
    override val index: Int,
    override val title: String,
    override val description: String,
) : Permission {
  IDENTITY_PROVIDER(
      index = 0,
      title = "Identity provider",
      description = "Grants access to all users' metadata, specifically that which is" +
          " necessary to issue JWTs.",
  ),
  SUPERUSER(
      index = 1,
      title = "Superuser",
      description = "Catch-all permission that overrides user, org, and feature checks. Users" +
          " with this permission can essentially impersonate any other user, and have additional" +
          " permissions as well.",
  );
}
