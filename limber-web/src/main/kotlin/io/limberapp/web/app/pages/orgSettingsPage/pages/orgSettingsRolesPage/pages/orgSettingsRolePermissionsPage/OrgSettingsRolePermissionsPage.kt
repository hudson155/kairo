package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.components.orgRolePermissionsSelector.orgRolePermissionsSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.util.Page
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.div
import react.router.dom.*

internal fun RBuilder.orgSettingsRolePermissionsPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsRolePermissionsPage : Page {
  internal data class PageParams(val roleSlug: String) : RProps

  fun path(roleSlug: String?) = listOf(
    OrgSettingsRolesPage.path,
    roleSlug ?: ":${PageParams::roleSlug.name}",
    "permissions"
  ).joinToString("/")
}

private class S : Styles("OrgSettingsRolePermissionsPage") {
  val container by css {
    margin(horizontal = 24.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val history = useHistory()
  val match = checkNotNull(useRouteMatch<OrgSettingsRolePermissionsPage.PageParams>())

  val (orgRoles, orgRolesMutator) = useOrgRolesState()

  val goBack = { history.goBack() }

  orgSettingsRolesListPage() // This page is a modal over the list page, so render the list page.

  val roleSlug = match.params.roleSlug
  val orgRole = orgRoles.values.singleOrNull { it.slug == roleSlug }
  if (orgRole == null) {
    redirect(to = OrgSettingsRolesPage.path)
    return
  }

  modal(onClose = goBack) {
    modalTitle(
      title = "Edit role: ${orgRole.name}",
      description = "Update the permissions this role grants."
    )
    div(classes = s.c { it::container }) {
      div {
        orgRolePermissionsSelector(
          permissions = orgRole.permissions,
          onChangePermissions = { orgRolesMutator.patch(orgRole.guid, OrgRoleRep.Update(permissions = it)) },
          onClose = goBack
        )
      }
    }
  }
}
