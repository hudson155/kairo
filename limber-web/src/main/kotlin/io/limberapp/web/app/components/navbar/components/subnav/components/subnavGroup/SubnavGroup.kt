package io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A group of items on a subnav. Items in the same group should be conceptually grouped. The physical spacing between
 * them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.subnavGroup(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val s = object : Styles("SubnavGroup") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottomStyle = BorderStyle.none
    }
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = s.c { it::container }) {
    props.children()
  }
}
