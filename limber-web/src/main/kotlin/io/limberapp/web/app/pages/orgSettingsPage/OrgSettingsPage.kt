package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.orgSettingsPage.components.orgSettingsSidenav.orgSettingsSidenav
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.OrgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage.orgSettingsInfoPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.orgSettingsRolesPage
import io.limberapp.web.auth.useAuth
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.orgSettingsPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsPage : Page {
  const val name = "Organization settings"
  const val path = "/settings/org"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()

  // Choose the default page to redirect to using a priority order, without redirecting the user to a page they don't
  // have permission to visit.
  val redirectTo = listOf(
    OrgSettingsInfoPage to OrgSettingsInfoPage.path,
    OrgSettingsRolesPage to OrgSettingsRolesPage.path
  ).firstOrNull { auth.canVisit(it.first) }?.second ?: return notFoundPage()

  standardLayout(leftPane = buildElement { orgSettingsSidenav() }) {
    switch {
      route(path = OrgSettingsPage.path, exact = true) { redirect(to = redirectTo) }
      route(path = OrgSettingsInfoPage.path, exact = true) { buildElement { orgSettingsInfoPage() } }
      route(path = OrgSettingsRolesPage.path) { buildElement { orgSettingsRolesPage() } }
    }
  }
}
