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
 */
internal fun RBuilder.sidenav(title: String, children: RHandler<RProps>) {
  child(component, Props(title), handler = children)
}

internal data class Props(val title: String) : RProps

private val s = object : Styles("Sidenav") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::container }) {
    sidenavGroup { sidenavItem { b { +props.title } } }
    props.children()
  }
}
