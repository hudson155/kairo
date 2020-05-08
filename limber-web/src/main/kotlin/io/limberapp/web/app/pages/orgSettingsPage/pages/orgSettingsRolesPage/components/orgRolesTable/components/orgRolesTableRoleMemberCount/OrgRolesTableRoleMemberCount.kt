package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.util.Strings
import io.limberapp.web.util.globalStyles
import io.limberapp.web.util.pluralize
import react.RBuilder
import react.RProps
import react.child
import react.dom.td
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.useRouteMatch
import styled.getClassName

/**
 * Portion of org roles table that shows the number of members, with a modal link.
 */
internal fun RBuilder.orgRolesTableRoleMemberCount(orgRole: OrgRoleRep.Complete) {
    child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
    val match = checkNotNull(useRouteMatch<RProps>())
    td {
        navLink<RProps>(
            to = "${match.url}/${props.orgRole.slug}",
            className = globalStyles.getClassName { it::link }
        ) {
            +props.orgRole.memberCount.let {
                "$it ${Strings.members.pluralize(it)}"
            }
        }
    }
}
