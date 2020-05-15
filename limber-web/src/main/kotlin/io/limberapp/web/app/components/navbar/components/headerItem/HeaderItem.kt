package io.limberapp.web.app.components.navbar.components.headerItem

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A single item on a top-of-page navbar.
 */
internal fun RBuilder.headerItem(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("HeaderItem") {
  val container by css {
    display = Display.flex
    alignItems = Align.center
    marginRight = 16.px
    color = Theme.Color.Text.light
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = styles.getClassName { it::container }) {
    b { props.children() }
  }
}
