package io.limberapp.web.app.components.sidenav.components.sidenavLink

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.sidenavLink(text: String, to: String) {
  child(component, Props(text, to))
}

internal data class Props(val text: String, val to: String) : RProps

private class S : Styles("SidenavLink") {
  val navLink by css {
    color = Theme.Color.Text.link
    padding(8.px)
    borderLeft(2.px, BorderStyle.solid, Color.transparent)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottomStyle = BorderStyle.none
    }
    hover {
      backgroundColor = Theme.Color.Background.lightActive
    }
  }
  val activeNavLink by css {
    borderLeftColor = Theme.Color.smallActiveIndicator
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  navLink<RProps>(
    to = props.to,
    className = s.c { it::navLink },
    activeClassName = s.c { it::activeNavLink }
  ) {
    span { +props.text }
  }
}
