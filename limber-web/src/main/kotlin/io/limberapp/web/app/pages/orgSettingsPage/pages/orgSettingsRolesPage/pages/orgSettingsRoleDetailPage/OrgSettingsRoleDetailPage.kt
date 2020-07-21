package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage

import com.piperframework.util.slugify
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.orgRoleMembersSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRolePermissionsSelector.orgRolePermissionsSelector
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.orgRoles.loadOrgRoles
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.componentWithApi
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

private class S : Styles("OrgSettingsRoleDetailPage") {
  val tabbedViewContainer by css {
    margin(horizontal = 24.px)
  }
}

private val s = S().apply { inject() }

private val component = componentWithApi(RBuilder::component)
private fun RBuilder.component(self: ComponentWithApi, props: RProps) {
  val history = useHistory()
  val match = checkNotNull(useRouteMatch<OrgSettingsRoleDetailPage.PageParams>())

  self.loadOrgRoles()

  val goBack = { history.goBack() }

  orgSettingsRolesListPage() // This page is a modal over the list page, so render the list page.

  // While the org roles are loading, show a blank modal.
  val orgRoles = self.gs.orgRoles.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return modal(blank = true, onClose = goBack) {}
      is LoadableState.Error -> return modal(onClose = goBack) { failedToLoad("roles") }
      is LoadableState.Loaded -> return@let loadableState.state.values.toSet()
    }
  }

  val roleSlug = match.params.roleSlug
  val orgRole = orgRoles.singleOrNull { it.slug == roleSlug }
  if (orgRole == null) {
    redirect(to = OrgSettingsRolesPage.path)
    return@component
  }

  modal(onClose = goBack) {
    modalTitle(
      title = "Edit role: ${orgRole.name}",
      description = "Update role info, including the permissions it grants and members of the role."
    )
    div(classes = s.c { it::tabbedViewContainer }) {
      div {
        when (match.params.tabName) {
          OrgSettingsRoleDetailPage.TabName.permissions.slugify() ->
            orgRolePermissionsSelector(orgRole, onClose = goBack)
          OrgSettingsRoleDetailPage.TabName.members.slugify() ->
            orgRoleMembersSelector(orgRole, onClose = goBack)
          else -> redirect(to = OrgSettingsRolesPage.path)
        }
      }
    }
  }
}
