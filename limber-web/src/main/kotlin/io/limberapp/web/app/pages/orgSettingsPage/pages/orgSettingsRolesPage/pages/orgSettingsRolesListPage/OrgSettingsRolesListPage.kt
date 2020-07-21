package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleCreationPage.OrgSettingsRoleCreationPage
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.orgRoles.loadOrgRoles
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.componentWithApi
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesListPage() {
  child(component)
}

private class S : Styles("OrgSettingsRolesListPage") {
  val addRoleButton by css {
    alignSelf = Align.flexEnd
    marginTop = 8.px
    marginRight = 8.px
  }
  val content by css {
    maxWidth = 768.px
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}

private val s = S().apply { inject() }

private val component = componentWithApi(RBuilder::component)
private fun RBuilder.component(self: ComponentWithApi, props: RProps) {
  val history = useHistory();

  self.loadOrgRoles()

  layoutTitle(OrgSettingsRolesPage.name, "Roles grant users permissions within your organization.")

  // While the org roles are loading, show a loading spinner.
  val orgRoles = self.gs.orgRoles.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return loadingSpinner()
      is LoadableState.Error -> return failedToLoad("roles")
      is LoadableState.Loaded -> return@let loadableState.state.values.toSet()
    }
  }

  div(classes = s.c { it::content }) {
    limberButton(
      style = Style.PRIMARY,
      onClick = { history.push(OrgSettingsRoleCreationPage.path) },
      classes = s.c { it::addRoleButton }
    ) { +"Create role" }
    orgRolesTable(orgRoles)
  }
}
