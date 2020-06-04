package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.orgRole.load
import io.limberapp.web.util.componentWithApi
import react.*

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesListPage() {
  child(component)
}

private val component = componentWithApi<RProps> component@{ self, _ ->
  self.load(self.gs.orgRoles)

  layoutTitle(OrgSettingsRolesPage.name, "Roles grant users permissions within your organization.")

  // While the org roles are loading, show a loading spinner.
  val orgRoles = self.gs.orgRoles.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return@component loadingSpinner()
      is LoadableState.Error -> return@component failedToLoad("roles")
      is LoadableState.Loaded -> return@let loadableState.state.values.toSet()
    }
  }

  orgRolesTable(orgRoles)
}
