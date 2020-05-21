package io.limberapp.web.app.components.sidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavItem.sidenavItem
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * Sidenav for navigation within a feature.
 *
 * The [title] is shown in its own [sidenavGroup] above everything else.
 *
 * [children] should be a series of [sidenavGroup]s.
 */
internal fun RBuilder.sidenav(title: String, children: RHandler<RProps>) {
  child(component, Props(title), handler = children)
}

internal data class Props(val title: String) : RProps

private class S : Styles("Sidenav") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::container }) {
    sidenavGroup { sidenavItem { b { +props.title } } }
    props.children()
  }
}
