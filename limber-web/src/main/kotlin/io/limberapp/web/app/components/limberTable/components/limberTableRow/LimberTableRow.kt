package io.limberapp.web.app.components.limberTable.components.limberTableRow

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A thin wrapper for the HTML [tr] element.
 *
 * [classes] is for CSS classes to apply.
 *
 * [children] is the contents of the [tr].
 */
internal fun RBuilder.limberTableRow(classes: String? = null, children: RHandler<RProps>) {
  child(component, Props(classes), handler = children)
}

internal data class Props(val classes: String?) : RProps

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

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  tr(classes = cls(s.c { it::row }, props.classes)) {
    props.children()
  }
}
