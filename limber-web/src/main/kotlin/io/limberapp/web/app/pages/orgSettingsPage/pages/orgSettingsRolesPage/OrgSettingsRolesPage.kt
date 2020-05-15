package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.OrgSettingsRoleDetailPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.orgSettingsRoleDetailPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import react.*
import react.router.dom.*

/**
 * Parent page for organization role and organization role membership settings.
 */
internal fun RBuilder.orgSettingsRolesPage() {
  child(component)
}

internal object OrgSettingsRolesPage {
  const val name = "Roles & permissions"
  const val path = "${OrgSettingsPage.path}/roles"
}

private val component = functionalComponent<RProps> {
  val match = checkNotNull(useRouteMatch<RProps>())

  switch {
    route(path = match.path, exact = true) {
      buildElement { orgSettingsRolesListPage() }
    }
    route(
      path = listOf(
        match.path,
        ":${OrgSettingsRoleDetailPage.PageParams::roleSlug.name}",
        ":${OrgSettingsRoleDetailPage.PageParams::tabName.name}"
      ).joinToString("/"),
      exact = true
    ) {
      buildElement { orgSettingsRoleDetailPage() }
    }
  }
}
