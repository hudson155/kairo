package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.orgRolesTableRow
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.TableLayout
import kotlinx.css.height
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.tableLayout
import kotlinx.css.width
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
    tableLayout = TableLayout.fixed
    maxWidth = 768.px
    kotlinx.css.th {
      height = 24.px
      padding(4.px)
      nthChild("2") { width = 160.px }
      nthChild("3") { width = 160.px }
    }
    kotlinx.css.td {
      height = 24.px
      padding(4.px)
      nthChild("2") { width = 160.px }
      nthChild("3") { width = 160.px }
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  if (props.orgRoles.isEmpty()) {
    p { +"No roles are defined." }
    return@functionalComponent
  }

  table(classes = s.c { it::table }) {
    thead {
      tr {
        th { +"Name" }
        th { +"Permissions" }
        th { +"Members" }
      }
    }
    tbody {
      props.orgRoles.sortedBy { it.uniqueSortKey }.forEach { orgRole ->
        orgRolesTableRow {
          attrs.key = orgRole.guid
          orgRolesTableRoleName(orgRole)
          orgRolesTableRolePermissions(orgRole)
          orgRolesTableRoleMemberCount(orgRole)
        }
      }
    }
  }
}
