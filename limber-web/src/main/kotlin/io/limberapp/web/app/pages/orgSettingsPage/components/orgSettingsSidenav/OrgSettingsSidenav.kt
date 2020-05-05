package io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.ORG_SETTINGS_INFO_PAGE_NAME
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.ORG_SETTINGS_ROLE_PAGE_NAME
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Sidenav for navigation within org settings pages.
 */
internal fun RBuilder.orgSettingsSidenav() {
    child(orgSettingsSidenav)
}

private val orgSettingsSidenav = functionalComponent<RProps> {
    sidenav("Organization settings") {
        sidenavGroup {
            sidenavLink(to = "/settings/org/info") { +ORG_SETTINGS_INFO_PAGE_NAME }
            sidenavLink(to = "/settings/org/roles") { +ORG_SETTINGS_ROLE_PAGE_NAME }
        }
    }
}
