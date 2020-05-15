package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * An individual row for an org role in the org roles table.
 */
internal fun RBuilder.orgRolesTableRow(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("OrgRolesTableRow") {
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
  tr(classes = styles.getClassName { it::row }) {
    props.children()
  }
}
