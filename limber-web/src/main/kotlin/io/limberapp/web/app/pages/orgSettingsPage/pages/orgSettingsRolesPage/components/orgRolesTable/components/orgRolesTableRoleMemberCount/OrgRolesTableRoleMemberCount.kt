package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRoleMemberCount

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import io.limberapp.web.util.pluralize
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.orgRolesTableRoleMemberCount(
  memberCount: Int,
  linkTo: String,
  classes: String? = null
) {
  child(component, Props(memberCount, linkTo, classes))
}

internal data class Props(
  val memberCount: Int,
  val linkTo: String,
  val classes: String?
) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  limberTableCell(classes = props.classes) {
    navLink<RProps>(to = props.linkTo, className = cls(gs.c { it::link }, gs.c { it::hiddenXs })) {
      +props.memberCount.let {
        "$it ${"members".pluralize(it)}"
      }
    }
    span(classes = gs.c { it::visibleXs }) {
      inlineIcon("users", rightMargin = true)
      +props.memberCount.toString()
    }
  }
}
