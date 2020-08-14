package io.limberapp.web.auth

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.OrgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.util.Page

internal data class AuthContext(
  val isLoading: Boolean,
  val isAuthenticated: Boolean,
  val signIn: () -> Unit,
  val jwt: Jwt?,
  val signOut: () -> Unit,
) {
  val featurePermissions by lazy { checkNotNull(jwt).org.features.mapValues { it.value.permissions } }

  val orgPermissions by lazy { checkNotNull(jwt).org.permissions }

  fun canVisit(page: Page): Boolean {
    when (page) {
      is OrgSettingsPage -> return setOf(OrgSettingsInfoPage, OrgSettingsRolesPage).any { canVisit(it) }
      is OrgSettingsInfoPage -> return OrgPermission.MANAGE_ORG_METADATA in orgPermissions
      is OrgSettingsRolesPage -> return setOf(
        OrgPermission.MANAGE_ORG_ROLES,
        OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS
      ).any { it in orgPermissions }
      else -> error("Permissions for page $page are not understood.")
    }
  }
}
