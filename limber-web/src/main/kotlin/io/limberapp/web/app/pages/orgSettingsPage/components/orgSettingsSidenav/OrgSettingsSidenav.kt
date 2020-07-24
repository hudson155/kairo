package io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.OrgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.auth.useAuth
import react.*

internal fun RBuilder.orgSettingsSidenav() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()

  sidenav(OrgSettingsPage.name) {
    sidenavGroup {
      if (auth.canVisit(OrgSettingsInfoPage)) {
        sidenavLink(OrgSettingsInfoPage.name, to = OrgSettingsInfoPage.path)
      }
      if (auth.canVisit(OrgSettingsRolesPage)) {
        sidenavLink(OrgSettingsRolesPage.name, to = OrgSettingsRolesPage.path)
      }
    }
  }
}
