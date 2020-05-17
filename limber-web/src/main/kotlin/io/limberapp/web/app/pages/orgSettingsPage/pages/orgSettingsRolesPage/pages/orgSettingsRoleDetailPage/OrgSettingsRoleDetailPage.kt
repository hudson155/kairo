package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage

import com.piperframework.util.slugify
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.components.tabbedView.tabbedView
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.orgRoleMembersSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRolePermissionsSelector.orgRolePermissionsSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.withContext
import kotlinx.css.*
import react.*
import react.dom.div
import react.router.dom.*

/**
 * Page for managing a single organization role and its memberships.
 */
internal fun RBuilder.orgSettingsRoleDetailPage() {
  child(component)
}

internal object OrgSettingsRoleDetailPage {
  internal data class PageParams(val roleSlug: String, val tabName: String) : RProps
  internal object TabName {
    const val permissions = "Permissions"
    const val members = "Members"
  }
}

private val s = object : Styles("OrgSettingsRoleDetailPage") {
  val tabbedViewContainer by css {
    margin(horizontal = 24.px)
  }
}.apply { inject() }

private val component = functionalComponent<RProps> {
  val api = useApi()
  val global = useGlobalState()
  val history = useHistory()
  val match = checkNotNull(useRouteMatch<OrgSettingsRoleDetailPage.PageParams>())

  val goBack = { history.goBack() }

  withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

  orgSettingsRolesListPage() // This page is a modal over the list page, so render the list page.

  // While the org roles are loading, show a blank modal.
  val orgRoles = global.state.orgRoles.let { loadableState ->
    if (!loadableState.isLoaded) {
      modal(blank = true, onClose = goBack) {}
      return@functionalComponent
    }
    return@let checkNotNull(loadableState.state).values.toSet()
  }

  val roleSlug = match.params.roleSlug
  val orgRole = orgRoles.singleOrNull { it.slug == roleSlug }
  if (orgRole == null) {
    redirect(to = OrgSettingsRolesPage.path)
    return@functionalComponent
  }

  modal(onClose = goBack) {
    modalTitle(
      title = "Edit role: ${orgRole.name}",
      description = "Update role info, including the permissions it grants and members of the role."
    )
    div(classes = s.c { it::tabbedViewContainer }) {
      tabbedView(OrgSettingsRoleDetailPage.TabName.permissions, OrgSettingsRoleDetailPage.TabName.members)
      div {
        when (match.params.tabName) {
          OrgSettingsRoleDetailPage.TabName.permissions.slugify() ->
            orgRolePermissionsSelector(orgRole, onClose = goBack)
          OrgSettingsRoleDetailPage.TabName.members.slugify() ->
            orgRoleMembersSelector(orgRole)
          else -> redirect(to = OrgSettingsRolesPage.path)
        }
      }
    }
  }
}
