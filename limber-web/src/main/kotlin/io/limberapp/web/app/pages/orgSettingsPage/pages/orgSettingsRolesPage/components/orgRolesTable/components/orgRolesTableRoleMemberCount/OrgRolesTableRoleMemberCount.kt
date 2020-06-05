package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount

import com.piperframework.util.slugify
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.OrgSettingsRoleDetailPage
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.component
import io.limberapp.web.util.gs
import io.limberapp.web.util.pluralize
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Portion of org roles table that shows the number of members, with a modal link.
 *
 * [orgRole] is the role to be represented by this component.
 *
 * [classes] is for CSS classes to apply.
 */
internal fun RBuilder.orgRolesTableRoleMemberCount(orgRole: OrgRoleRep.Complete, classes: String? = null) {
  child(component, Props(orgRole, classes))
}

internal data class Props(val orgRole: OrgRoleRep.Complete, val classes: String?) : RProps

private val component = component<Props> component@{ props ->
  val match = checkNotNull(useRouteMatch<RProps>())
  td(classes = props.classes) {
    navLink<RProps>(
      to = "${match.url}/${props.orgRole.slug}/${OrgSettingsRoleDetailPage.TabName.members.slugify()}",
      className = cls(gs.c { it::link }, gs.c { it::hiddenXs })
    ) {
      +props.orgRole.memberCount.let {
        "$it ${"members".pluralize(it)}"
      }
    }
    span(classes = gs.c { it::visibleXs }) {
      inlineIcon("users", rightMargin = true)
      +props.orgRole.memberCount.toString()
    }
  }
}
