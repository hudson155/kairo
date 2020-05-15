package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.withContext
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesListPage() {
  child(component)
}

private val component = functionalComponent<RProps> {
  val api = useApi()
  val global = useGlobalState()

  withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

  layoutTitle(OrgSettingsRolesPage.name, "Roles grant users permissions within your organization.")

  // While the org roles are loading, show nothing else.
  val orgRoles = global.state.orgRoles.let { loadableState ->
    if (!loadableState.isLoaded) return@functionalComponent
    return@let checkNotNull(loadableState.state).values.toSet()
  }

  orgRolesTable(orgRoles)
}
