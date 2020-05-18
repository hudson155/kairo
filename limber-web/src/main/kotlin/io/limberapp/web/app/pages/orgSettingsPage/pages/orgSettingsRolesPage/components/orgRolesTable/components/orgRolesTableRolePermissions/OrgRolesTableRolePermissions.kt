package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions

import com.piperframework.util.slugify
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.OrgSettingsRoleDetailPage
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.pluralize
import react.*
import react.dom.td
import react.router.dom.*

/**
 * Portion of org roles table that shows the number of permissions, with a modal link.
 */
internal fun RBuilder.orgRolesTableRolePermissions(orgRole: OrgRoleRep.Complete) {
  child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
  val match = checkNotNull(useRouteMatch<RProps>())
  td {
    navLink<RProps>(
      to = "${match.url}/${props.orgRole.slug}/${OrgSettingsRoleDetailPage.TabName.permissions.slugify()}",
      className = gs.c { it::link }
    ) {
      +props.orgRole.permissions.size.let {
        "$it ${"permissions".pluralize(it)}"
      }
    }
  }
}
