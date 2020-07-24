package io.limberapp.web.app.components.limberTable

import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * [headers] renders a column header for each element, and a blank column header for each null element. These are not
 * visible below the xs screen size.
 *
 * [children] is the contents of the [table]'s [tbody]. In order to support proper rendering, each [tr] must have the
 * same number of [td]s as the length of [headers].
 */
internal fun RBuilder.limberTable(headers: List<String?>?, classes: String? = null, children: RHandler<Props>) {
  child(component, Props(headers, classes), handler = children)
}

internal data class Props(val headers: List<String?>?, val classes: String?) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  table(classes = props.classes) {
    props.headers?.let { headers ->
      thead(classes = gs.c { it::hiddenXs }) {
        tr {
          headers.forEach { header ->
            limberTableCell(header = true, hideContent = header == null) { header?.let { +it } }
          }
        }
      }
    }
    tbody {
      props.children()
    }
  }
}
