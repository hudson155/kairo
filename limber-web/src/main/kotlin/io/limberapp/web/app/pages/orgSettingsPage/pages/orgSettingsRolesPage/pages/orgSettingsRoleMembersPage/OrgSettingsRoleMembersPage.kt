package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage

import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.components.orgRoleMembersSelector.orgRoleMembersSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.OrgSettingsRolePermissionsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.state.state.orgRoleMemberships.orgRoleMembershipsStateProvider
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.state.state.orgState.useOrgState
import io.limberapp.web.util.Page
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.div
import react.router.dom.*

internal fun RBuilder.orgSettingsRoleMembersPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsRoleMembersPage : Page {
  internal data class PageParams(val roleSlug: String) : RProps

  fun path(roleSlug: String?) = listOf(
    OrgSettingsRolesPage.path,
    roleSlug ?: ":${OrgSettingsRolePermissionsPage.PageParams::roleSlug.name}",
    "members"
  ).joinToString("/")
}

private class S : Styles("OrgSettingsRoleMembersPage") {
  val container by css {
    margin(horizontal = 24.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()
  val history = useHistory()
  val match = checkNotNull(useRouteMatch<OrgSettingsRoleMembersPage.PageParams>())

  val (org, _) = useOrgState()
  val (orgRoles, _) = useOrgRolesState()

  val goBack = { history.goBack() }

  orgSettingsRolesListPage() // This page is a modal over the list page, so render the list page.

  val roleSlug = match.params.roleSlug
  val orgRole = orgRoles.values.singleOrNull { it.slug == roleSlug }
  if (orgRole == null) {
    redirect(to = OrgSettingsRolesPage.path)
    return
  }

  val orgRoleMemberships = load { api(OrgRoleMembershipApi.GetByOrgRoleGuid(org.guid, orgRole.guid)) }

  // While the org role memberships are loading, show a loading spinner.
  (orgRoleMemberships ?: return loadingSpinner()).onFailure { return failedToLoad("org role memberships") }

  orgRoleMembershipsStateProvider(orgRoleMemberships.value.associateBy { it.accountGuid }) {
    modal(onClose = goBack) {
      modalTitle(
        title = "Edit role: ${orgRole.name}",
        description = "Update the members of this role."
      )
      div(classes = s.c { it::container }) {
        div {
          orgRoleMembersSelector(orgRoleGuid = orgRole.guid, onClose = goBack)
        }
      }
    }
  }
}
