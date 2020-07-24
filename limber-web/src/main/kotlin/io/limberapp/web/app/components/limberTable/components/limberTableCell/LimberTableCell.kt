package io.limberapp.web.app.components.limberTable.components.limberTableCell

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * If [header] is true, this will use [th] instead of [td].
 *
 * If [isEmpty] is true, the cell will be rendered without padding and ignoring the [children].
 */
internal fun RBuilder.limberTableCell(
  header: Boolean = false,
  hideContent: Boolean = false,
  classes: String? = null,
  children: RHandler<Props>
) {
  child(component, Props(header, hideContent, classes), handler = children)
}

internal data class Props(val header: Boolean, val hideContent: Boolean, val classes: String?) : RProps

private class S : Styles("LimberTableCell") {
  val cell by css {
    padding(4.px)
  }
  val emptyCell by css {
    padding(0.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val classes = cls(s.c { it::cell }, s.c(props.hideContent) { it::emptyCell }, props.classes)
  if (props.header) {
    th(classes = classes) {
      if (!props.hideContent) props.children()
    }
  } else {
    td(classes = classes) {
      if (!props.hideContent) props.children()
    }
  }
}
