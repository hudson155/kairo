package io.limberapp.web.app.components.navbar.components.headerGroup

import io.limberapp.web.util.Styles
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A group of items on a top-of-page navbar. Items in the same group should be conceptually grouped. The physical
 * spacing between them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.headerGroup(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("HeaderGroup") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    margin(vertical = 0.px, horizontal = 16.px)
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = styles.getClassName { it::container }) {
    props.children()
  }
}
