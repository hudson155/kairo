package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.util.pluralize
import react.RBuilder
import react.RProps
import react.child
import react.dom.td
import react.functionalComponent

internal fun RBuilder.orgRolesTableRoleMemberCount(orgRole: OrgRoleRep.Complete) {
    child(orgRolesTableRoleMemberCount, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val orgRolesTableRoleMemberCount = functionalComponent<Props> { props ->
    td {
        +props.orgRole.memberCount.let {
            "$it ${pluralize("members", it)}"
        }
    }
}
