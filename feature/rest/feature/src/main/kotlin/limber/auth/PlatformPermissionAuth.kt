package limber.auth

/**
 * Protects an endpoint behind a particular platform-level permission.
 */
public class PlatformPermissionAuth(
  permission: PlatformPermission,
) : Auth.Permission<PlatformPermission>(permission) {
  override fun getPermissions(context: RestContext): List<PlatformPermission>? =
    context.getClaim<List<PlatformPermission?>>("permissions")?.filterNotNull()
}
