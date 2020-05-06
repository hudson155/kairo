package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleName

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import react.RBuilder
import react.RProps
import react.child
import react.dom.td
import react.functionalComponent

internal fun RBuilder.orgRolesTableRoleName(orgRole: OrgRoleRep.Complete) {
    child(orgRolesTableRoleName, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val orgRolesTableRoleName = functionalComponent<Props> { props ->
    td {
        +props.orgRole.name
    }
}
