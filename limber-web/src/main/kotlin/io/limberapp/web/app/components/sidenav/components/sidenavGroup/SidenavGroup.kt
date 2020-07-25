package io.limberapp.web.app.components.sidenav.components.sidenavGroup

import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * [children] should be a series of [sidenavLink]s, probably including some <a> tags.
 */
internal fun RBuilder.sidenavGroup(children: RHandler<Props>) {
  child(component, handler = children)
}

internal typealias Props = RProps

private class S : Styles("SidenavGroup") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    backgroundColor = Theme.Color.Background.white
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    marginBottom = 16.px
    overflow = Overflow.hidden // Avoid background color overflow.
    lastOfType {
      marginBottom = 0.px
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = s.c { it::container }) {
    props.children()
  }
}
