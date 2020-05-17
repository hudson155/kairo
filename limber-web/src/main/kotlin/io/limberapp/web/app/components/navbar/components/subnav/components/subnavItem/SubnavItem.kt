package io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A single item on a subnav. If [hoverable] is true, it will become accented when hovered.
 */
internal fun RBuilder.subnavItem(hoverable: Boolean = true, children: RHandler<Props>) {
  child(component, Props(hoverable), handler = children)
}

internal data class Props(val hoverable: Boolean) : RProps

private val s = object : Styles("SubnavItem") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    alignItems = Align.flexStart
    marginTop = 4.px
    padding(vertical = 4.px, horizontal = 8.px)
    lastOfType {
      marginBottom = 4.px
    }
  }
  val hoverableContainer by css {
    hover {
      color = Theme.Color.Text.light
      backgroundColor = Theme.Color.Background.link
    }
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = cls(s.c { it::container }, if (props.hoverable) s.c { it::hoverableContainer } else null)) {
    props.children()
  }
}
