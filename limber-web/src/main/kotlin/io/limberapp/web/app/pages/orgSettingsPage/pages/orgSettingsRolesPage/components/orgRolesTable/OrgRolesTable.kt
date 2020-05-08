package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.orgRolesTableRow
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Strings
import io.limberapp.web.util.Styles
import io.limberapp.web.util.withContext
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
internal fun RBuilder.orgRolesTable() {
    child(component)
}

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

private val component = functionalComponent<RProps> {
    val api = useApi()
    val global = useGlobalState()

    withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

    // While the org roles are loading, show nothing.
    if (!global.state.orgRoles.isLoaded) return@functionalComponent

    val orgRoles = checkNotNull(global.state.orgRoles.state)

    if (orgRoles.isEmpty()) {
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
            orgRoles.values.sortedByDescending { it.createdDate }.forEach { orgRole ->
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
