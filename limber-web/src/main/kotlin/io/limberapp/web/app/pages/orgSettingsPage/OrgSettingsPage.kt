package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav.orgSettingsSidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.orgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.orgSettingsRolePage
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
    child(orgSettingsPage)
}

private val orgSettingsPage = functionalComponent<RProps> {
    standardLayout(leftPane = buildElement { orgSettingsSidenav() }) {
        switch {
            route(path = "/settings/org", exact = true) { redirect(from = "/", to = "/settings/org/info") }
            route(path = "/settings/org/info", exact = true) { buildElement { orgSettingsInfoPage() } }
            route(path = "/settings/org/roles", exact = true) { buildElement { orgSettingsRolePage() } }
        }
    }
}
