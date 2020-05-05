package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.orgRolesTableRow
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.withContext
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.dom.tbody
import react.dom.thead
import react.dom.tr
import react.functionalComponent
import styled.css
import styled.styledTable
import styled.styledTh

internal fun RBuilder.orgRolesTable() {
    child(orgRolesTable)
}

private val orgRolesTable = functionalComponent<RProps> {
    val api = useApi()
    val global = useGlobalState()

    withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

    // While the org roles are loading, show nothing.
    if (!global.state.orgRoles.isLoaded) return@functionalComponent

    val orgRoles = checkNotNull(global.state.orgRoles.state)

    if (orgRoles.isEmpty()) {
        p { +"No roles are defined." }
        return@functionalComponent
    }

    styledTable {
        css {
            maxWidth = 768.px
            kotlinx.css.th {
                padding(4.px)
            }
            kotlinx.css.td {
                padding(4.px)
            }
        }
        thead {
            tr {
                styledTh { +"Name" }
                styledTh { +"Permissions" }
                styledTh { +"Members" }
            }
        }
        tbody {
            orgRoles.forEach {
                orgRolesTableRow {
                    orgRolesTableRoleName(it)
                    orgRolesTableRolePermissions(it)
                    orgRolesTableRoleMemberCount(it)
                }
            }
        }
    }
}
