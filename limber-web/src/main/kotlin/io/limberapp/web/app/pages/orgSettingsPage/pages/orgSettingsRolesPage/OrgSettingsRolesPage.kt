package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleCreationPage.OrgSettingsRoleCreationPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleCreationPage.orgSettingsRoleCreationPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.OrgSettingsRoleMembersPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.orgSettingsRoleMembersPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.OrgSettingsRolePermissionsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.orgSettingsRolePermissionsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.state.state.orgRoles.orgRolesStateProvider
import io.limberapp.web.state.state.org.useOrgState
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.orgSettingsRolesPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsRolesPage : Page {
  const val name = "Roles & permissions"
  const val path = "${OrgSettingsPage.path}/roles"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (org, _) = useOrgState()

  val orgRoles = load { api(OrgRoleApi.GetByOrgGuid(org.guid)) }

  // While the org roles are loading, show a loading spinner.
  (orgRoles ?: return loadingSpinner()).onFailure { return failedToLoad("org roles") }

  orgRolesStateProvider(orgRoles.value.associateBy { it.guid }) {
    switch {
      route(path = OrgSettingsRolesPage.path, exact = true) {
        buildElement { orgSettingsRolesListPage() }
      }
      route(path = OrgSettingsRolePermissionsPage.path(null), exact = true) {
        buildElement { orgSettingsRolePermissionsPage() }
      }
      route(path = OrgSettingsRoleMembersPage.path(null), exact = true) {
        buildElement { orgSettingsRoleMembersPage() }
      }
      route(path = OrgSettingsRoleCreationPage.path, exact = true) {
        buildElement { orgSettingsRoleCreationPage() }
      }
    }
  }
}
