package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount.orgRolesTableRoleMemberCount
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName.orgRolesTableRoleName
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions.orgRolesTableRolePermissions
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.orgRolesTableRow
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.withContext
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.px
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

internal fun RBuilder.orgRolesTable() {
    child(orgRolesTable)
}

private val styles = object : Styles("OrgRolesTable") {
    val table by css {
        maxWidth = 768.px
        kotlinx.css.th {
            padding(4.px)
        }
        kotlinx.css.td {
            padding(4.px)
        }
    }
}.apply { inject() }

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

    table(classes = styles.getClassName { it::table }) {
        thead {
            tr {
                th { +"Name" }
                th { +"Permissions" }
                th { +"Members" }
            }
        }
        tbody {
            orgRoles.values.forEach {
                orgRolesTableRow {
                    attrs.key = it.guid
                    orgRolesTableRoleName(it)
                    orgRolesTableRolePermissions(it)
                    orgRolesTableRoleMemberCount(it)
                }
            }
        }
    }
}
