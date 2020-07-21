package io.limberapp.web.app.components.sidenav.components.sidenavHeader

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.xs
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * The header for a [sidenav]. Contains functionality to open and close the [sidenavLink] series that the [sidenav]
 * contains.
 *
 * [text] is the text to display.
 *
 * [isOpen] represents whether or not the dropdown is open.
 *
 * [onClick] is called to toggle the state of the [sidenav]'s dropdown.
 */
internal fun RBuilder.sidenavHeader(text: String, isOpen: Boolean, onClick: () -> Unit) {
  child(component, Props(text, isOpen, onClick))
}

internal data class Props(val text: String, val isOpen: Boolean, val onClick: () -> Unit) : RProps

private class S : Styles("SidenavHeader") {
  val container by css {
    display = Display.flex
    justifyContent = JustifyContent.spaceBetween
    backgroundColor = Theme.Color.Background.lightDisabled
    padding(8.px)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    xs {
      cursor = Cursor.pointer
    }
    lastOfType {
      borderBottomStyle = BorderStyle.none
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  sidenavGroup {
    a(classes = s.c { it::container }) {
      span { b { +props.text } }
      attrs.onClickFunction = { props.onClick() }
      inlineIcon(name = if (!props.isOpen) "angle-down" else "angle-up", classes = gs.c { it::visibleXs })
    }
  }
}
