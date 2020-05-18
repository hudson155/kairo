package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * An individual row for an org role in the org roles table.
 */
internal fun RBuilder.orgRolesTableRow(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val s = object : Styles("OrgRolesTableRow") {
  val row by css {
    borderTop(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    }
    hover {
      backgroundColor = Theme.Color.Background.lightActive
    }
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  tr(classes = s.c { it::row }) {
    props.children()
  }
}
