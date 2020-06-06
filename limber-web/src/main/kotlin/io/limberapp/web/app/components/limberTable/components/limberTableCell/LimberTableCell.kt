package io.limberapp.web.app.components.limberTable.components.limberTableCell

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.component
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A thin wrapper for the HTML [td] or [th] element.
 *
 * If [header] is true, this will use [th] instead of [td].
 *
 * If [isEmpty] is true, the cell will be rendered without padding and ignoring the [children].
 *
 * [classes] is for CSS classes to apply.
 *
 * [children] is the contents of the [td] or [th].
 */
internal fun RBuilder.limberTableCell(
  header: Boolean = false,
  isEmpty: Boolean = false,
  classes: String? = null,
  children: RHandler<RProps>
) {
  child(component, Props(header, isEmpty, classes), handler = children)
}

internal data class Props(val header: Boolean, val isEmpty: Boolean, val classes: String?) : RProps

private class S : Styles("LimberTableCell") {
  val cell by css {
    padding(4.px)
  }
  val emptyCell by css {
    padding(0.px)
  }
}

private val s = S().apply { inject() }

private val component = component<Props> component@{ props ->
  val classes = cls(s.c { it::cell }, s.c(props.isEmpty) { it::emptyCell }, props.classes)
  if (props.header) {
    th(classes = classes) {
      if (!props.isEmpty) props.children()
    }
  } else {
    td(classes = classes) {
      if (!props.isEmpty) props.children()
    }
  }
}
