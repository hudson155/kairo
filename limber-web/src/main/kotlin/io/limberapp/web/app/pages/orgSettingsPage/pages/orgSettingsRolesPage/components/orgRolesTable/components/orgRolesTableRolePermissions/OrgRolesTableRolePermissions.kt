package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions

import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.util.Strings
import io.limberapp.web.util.globalStyles
import io.limberapp.web.util.pluralize
import react.RBuilder
import react.RProps
import react.child
import react.dom.td
import react.functionalComponent
import react.router.dom.navLink
import styled.getClassName

/**
 * Portion of org roles table that shows the number of permissions, with a modal link.
 */
internal fun RBuilder.orgRolesTableRolePermissions(orgRole: OrgRoleRep.Complete) {
    child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
    td {
        navLink<RProps>(
            to = "${OrgSettingsRolesPage.path}/${enc(props.orgRole.name)}",
            className = globalStyles.getClassName { it::link }
        ) {
            +props.orgRole.permissions.size.let {
                "$it ${Strings.orgRolePermissions.pluralize(it)}"
            }
        }
    }
}
