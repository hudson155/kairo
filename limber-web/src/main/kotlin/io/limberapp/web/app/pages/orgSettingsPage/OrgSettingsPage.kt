package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav.orgSettingsSidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.OrgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.orgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.orgSettingsRolesPage
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * Parent page for organization-level settings.
 */
internal fun RBuilder.orgSettingsPage() {
  child(component)
}

internal object OrgSettingsPage {
  const val name = "Organization settings"
  const val path = "/settings/org"
}

private val component = component<RProps> component@{
  standardLayout(leftPane = buildElement { orgSettingsSidenav() }) {
    switch {
      route(path = OrgSettingsPage.path, exact = true) { redirect(to = OrgSettingsInfoPage.path) }
      route(path = OrgSettingsInfoPage.path, exact = true) { buildElement { orgSettingsInfoPage() } }
      route(path = OrgSettingsRolesPage.path) { buildElement { orgSettingsRolesPage() } }
    }
  }
}
