package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.util.Strings
import io.limberapp.web.util.pluralize
import react.RBuilder
import react.RProps
import react.child
import react.dom.td
import react.functionalComponent

/**
 * Portion of org roles table that shows the number of permissions, with a modal link.
 */
internal fun RBuilder.orgRolesTableRolePermissions(orgRole: OrgRoleRep.Complete) {
    child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
    td {
        +props.orgRole.permissions.permissions.size.let {
            "$it ${Strings.orgRolePermissions.pluralize(it)}"
        }
    }
}
