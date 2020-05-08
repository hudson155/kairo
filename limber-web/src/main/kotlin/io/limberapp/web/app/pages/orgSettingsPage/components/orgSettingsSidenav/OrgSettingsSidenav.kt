package io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.orgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.orgSettingsRolesPage
import io.limberapp.web.util.Strings
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Sidenav for navigation within org settings pages.
 */
internal fun RBuilder.orgSettingsSidenav() {
    child(component)
}

private val component = functionalComponent<RProps> {
    sidenav(Strings.orgSettings) {
        sidenavGroup {
            sidenavLink(to = orgSettingsInfoPage.path) { +orgSettingsInfoPage.name }
            sidenavLink(to = orgSettingsRolesPage.path) { +orgSettingsRolesPage.name }
        }
    }
}
