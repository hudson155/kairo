package io.limberapp.web.app.components.sidenav.components.sidenavGroup

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A group of items on a sidenav. Items in the same group should be conceptually grouped. The physical spacing between
 * them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.sidenavGroup(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("SidenavGroup") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    marginBottom = 16.px
    overflow = Overflow.hidden // Avoid background color overflow.
    lastOfType {
      marginBottom = 0.px
    }
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = styles.getClassName { it::container }) {
    props.children()
  }
}
