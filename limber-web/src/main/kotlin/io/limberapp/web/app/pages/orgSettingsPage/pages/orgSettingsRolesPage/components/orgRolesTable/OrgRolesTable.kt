package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.orgRolesTableRow
import io.limberapp.web.util.Strings
import io.limberapp.web.util.Styles
import kotlinx.css.TableLayout
import kotlinx.css.height
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.tableLayout
import kotlinx.css.width
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.dom.table
import react.dom.tbody
import react.dom.th
import react.dom.thead
import react.dom.tr
import react.functionalComponent
import react.key
import styled.getClassName

/**
 * A table showing org roles.
 */
internal fun RBuilder.orgRolesTable(orgRoles: Set<OrgRoleRep.Complete>) {
    child(component, Props(orgRoles))
}

internal data class Props(val orgRoles: Set<OrgRoleRep.Complete>) : RProps

private val styles = object : Styles("OrgRolesTable") {
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
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
    if (props.orgRoles.isEmpty()) {
        p { +Strings.noRolesAreDefined }
        return@functionalComponent
    }

    table(classes = styles.getClassName { it::table }) {
        thead {
            tr {
                th { +Strings.orgRoleNameTitle }
                th { +Strings.orgRolePermissionsTitle }
                th { +Strings.orgRoleMembersTitle }
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
