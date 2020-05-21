package io.limberapp.web.app.components.sidenav.components.sidenavItem

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A single non-link item on a sidenav. This goes inside a [sidenavGroup].
 *
 * [children] should just be some text.
 */
internal fun RBuilder.sidenavItem(children: RHandler<RProps>) {
  child(component, handler = children)
}

private class S : Styles("SidenavItem") {
  val container by css {
    backgroundColor = Theme.Color.Background.lightDisabled
    padding(8.px)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottomStyle = BorderStyle.none
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = s.c { it::container }) {
    props.children()
  }
}
