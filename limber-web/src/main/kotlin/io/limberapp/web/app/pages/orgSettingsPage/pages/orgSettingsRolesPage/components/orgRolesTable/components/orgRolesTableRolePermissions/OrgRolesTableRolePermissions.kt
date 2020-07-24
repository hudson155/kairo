package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRolePermissions

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import io.limberapp.web.util.pluralize
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.orgRolesTableRolePermissions(
  permissions: OrgPermissions,
  linkTo: String,
  classes: String? = null
) {
  child(component, Props(permissions, linkTo, classes))
}

internal data class Props(
  val permissions: OrgPermissions,
  val linkTo: String,
  val classes: String?
) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  limberTableCell(classes = cls(props.classes)) {
    navLink<RProps>(to = props.linkTo, className = cls(gs.c { it::link }, gs.c { it::hiddenXs })) {
      +props.permissions.size.let {
        "$it ${"permissions".pluralize(it)}"
      }
    }
    span(classes = gs.c { it::visibleXs }) {
      inlineIcon("check-circle", rightMargin = true)
      +props.permissions.size.toString()
    }
  }
}
