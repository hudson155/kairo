package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav.orgSettingsSidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.orgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.orgSettingsRolesPage
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch

/**
 * Parent page for organization-level settings.
 */
internal fun RBuilder.orgSettingsPage() {
    child(component)
}

internal val orgSettingsPage = Page(
    name = "Organization settings",
    path = "/settings/org"
)

private val component = functionalComponent<RProps> {
    standardLayout(leftPane = buildElement { orgSettingsSidenav() }) {
        switch {
            route(path = orgSettingsPage.path, exact = true) { redirect(to = orgSettingsInfoPage.path) }
            route(path = orgSettingsInfoPage.path, exact = true) { buildElement { orgSettingsInfoPage() } }
            route(path = orgSettingsRolesPage.path, exact = true) { buildElement { orgSettingsRolesPage() } }
        }
    }
}
