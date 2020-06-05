package io.limberapp.web.app.components.limberTable.components.limberTableRow

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.component
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A thin wrapper for the HTML [tr] element.
 *
 * [children] is the contents of the [tr].
 */
internal fun RBuilder.limberTableRow(children: RHandler<RProps>) {
  child(component, handler = children)
}

private class S : Styles("LimberTableRow") {
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
