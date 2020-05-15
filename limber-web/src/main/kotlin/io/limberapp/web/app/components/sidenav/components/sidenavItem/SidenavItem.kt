package io.limberapp.web.app.components.sidenav.components.sidenavItem

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A single non-link item on a sidenav.
 */
internal fun RBuilder.sidenavItem(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("SidenavItem") {
  val container by css {
    backgroundColor = Theme.Color.Background.lightDisabled
    padding(8.px)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    lastOfType {
      borderBottomStyle = BorderStyle.none
    }
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = styles.getClassName { it::container }) {
    props.children()
  }
}
