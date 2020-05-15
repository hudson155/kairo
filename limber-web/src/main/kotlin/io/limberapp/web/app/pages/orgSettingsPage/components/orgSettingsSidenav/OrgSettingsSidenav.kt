package io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.OrgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import react.*

/**
 * Sidenav for navigation within org settings pages.
 */
internal fun RBuilder.orgSettingsSidenav() {
  child(component)
}

private val component = functionalComponent<RProps> {
  sidenav(OrgSettingsPage.name) {
    sidenavGroup {
      sidenavLink(to = OrgSettingsInfoPage.path) { +OrgSettingsInfoPage.name }
      sidenavLink(to = OrgSettingsRolesPage.path) { +OrgSettingsRolesPage.name }
    }
  }
}
