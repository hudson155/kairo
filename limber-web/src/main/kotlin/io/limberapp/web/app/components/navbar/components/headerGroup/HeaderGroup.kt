package io.limberapp.web.app.components.navbar.components.headerGroup

import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.components.subnav.subnav
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A group of items on a top-of-page navbar. Items in the same group should be conceptually grouped. The physical
 * spacing between them will be less for items in the same group than for items in different groups.
 *
 * [children] should be a series of [headerItem]s, probably including some <a> tags and possibly including some
 * [subnav]s.
 */
internal fun RBuilder.headerGroup(children: RHandler<RProps>) {
  child(component, handler = children)
}

private class S : Styles("HeaderGroup") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    margin(vertical = 0.px, horizontal = 16.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: RProps) {
  div(classes = s.c { it::container }) {
    props.children()
  }
}
