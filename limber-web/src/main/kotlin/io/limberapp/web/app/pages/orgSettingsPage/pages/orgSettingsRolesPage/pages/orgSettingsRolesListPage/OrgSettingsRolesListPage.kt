package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleCreationPage.OrgSettingsRoleCreationPage
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.orgSettingsRolesListPage() {
  child(component)
}

internal typealias Props = RProps

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

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val history = useHistory()

  val (orgRoles, _) = useOrgRolesState()

  layoutTitle(OrgSettingsRolesPage.name, "Roles grant users permissions within your organization.")

  div(classes = s.c { it::content }) {
    limberButton(
      style = Style.PRIMARY,
      onClick = { history.push(OrgSettingsRoleCreationPage.path) },
      classes = s.c { it::addRoleButton }
    ) { +"Create role" }
    orgRolesTable(orgRoles.values.toSet())
  }
}
