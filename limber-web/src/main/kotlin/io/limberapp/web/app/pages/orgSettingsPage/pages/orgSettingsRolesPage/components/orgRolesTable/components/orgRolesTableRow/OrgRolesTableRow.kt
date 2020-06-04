package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.component
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * An individual row for an org role in the org roles table.
 *
 * [children] should always be the same series of components that make up the row.
 */
internal fun RBuilder.orgRolesTableRow(children: RHandler<RProps>) {
  child(component, handler = children)
}

private class S : Styles("OrgRolesTableRow") {
  val row by css {
    borderTop(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    }
    hover {
      backgroundColor = Theme.Color.Background.lightActive
    }
  }
}

private val s = S().apply { inject() }

private val component = component<RProps> component@{ props ->
  tr(classes = s.c { it::row }) {
    props.children()
  }
}
