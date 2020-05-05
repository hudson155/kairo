package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.util.pluralize
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledTd

internal fun RBuilder.orgRolesTableRolePermissions(orgRole: OrgRoleRep.Complete) {
    child(orgRolesTableRolePermissions, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val orgRolesTableRolePermissions = functionalComponent<Props> { props ->
    styledTd {
        +props.orgRole.permissions.permissions.size.let {
            "$it ${pluralize("permissions", it)}"
        }
    }
}
