package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.limberTable.components.limberTableRow.limberTableRow
import io.limberapp.web.app.components.limberTable.limberTable
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleTableRoleDeleter.orgRolesTableRoleDeleter
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.OrgSettingsRoleMembersPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.OrgSettingsRolePermissionsPage
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.notXs
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A table showing org roles, and allowing them to be edited by clicking on different parts of them.
 *
 * [orgRoles] is the set of roles to show on the table. One row for each.
 */
internal fun RBuilder.orgRolesTable(orgRoles: Set<OrgRoleRep.Complete>) {
  child(component, Props(orgRoles))
}

internal data class Props(val orgRoles: Set<OrgRoleRep.Complete>) : RProps

private class S : Styles("OrgRolesTable") {
  val table by css {
    th {
      notXs {
        nthChild("2") { width = 160.px }
        nthChild("3") { width = 160.px }
      }
    }
    td {
      notXs {
        nthChild("2") { width = 160.px }
        nthChild("3") { width = 160.px }
        nthChild("4") { width = 32.px }
      }
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (_, orgRolesMutator) = useOrgRolesState()

  if (props.orgRoles.isEmpty()) {
    p { +"No roles are defined." }
    return
  }

  p(classes = gs.c { it::visibleXs }) { +"Visit this page on a larger device to edit the roles." }

  limberTable(headers = listOf("Name", "Permissions", "Members", null), classes = s.c { it::table }) {
    props.orgRoles.sortedBy { it.uniqueSortKey }.forEach { orgRole ->
      limberTableRow {
        attrs.key = orgRole.guid
        orgRolesTableRoleName(
          name = orgRole.name,
          onSetName = { orgRolesMutator.patch(orgRole.guid, OrgRoleRep.Update(name = it)) }
        )
        orgRolesTableRolePermissions(
          permissions = orgRole.permissions,
          linkTo = OrgSettingsRolePermissionsPage.path(orgRole.slug)
        )
        orgRolesTableRoleMemberCount(
          memberCount = orgRole.memberCount,
          linkTo = OrgSettingsRoleMembersPage.path(orgRole.slug)
        )
        orgRolesTableRoleDeleter(
          name = orgRole.name,
          onDelete = { orgRolesMutator.delete(orgRole.guid) }
        )
      }
    }
  }
}
