package io.limberapp.web.app.components.navbar.components.subnav

import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import org.w3c.dom.Element
import react.*
import react.dom.*

/**
 * Generic navigational component that drops down from a top-of-page navbar. It's generally only visible when a nav link
 * is active, but that functionality must be managed outside the scope of this component.
 *
 * The [node] is a [RMutableRef] that will be assigned to the subnav div, allowing the parent component to detect clicks
 * outside of it.
 *
 * [children] should be a series of [subnavGroup]s.
 */
internal fun RBuilder.subnav(node: RMutableRef<Element?>, children: RHandler<RProps>) {
  child(component, Props(node), handler = children)
}

internal data class Props(val node: RMutableRef<Element?>) : RProps

// TODO: In order for this to be truly reusable the positioning likely needs to be altered.
private class S : Styles("Subnav") {
  val container by css {
    val widthPx = 192 // The width of this component.
    val afterOffsetPx = 22 // How far in the caret ::after element is.
    val centeringWidth = 32 // Center the caret under a component of this width.
    alignSelf = Align.flexStart
    position = Position.relative
    top = 44.px
    right = (widthPx - afterOffsetPx / 2 + centeringWidth / 2).px
    width = widthPx.px
    marginRight = (-widthPx - 2 * 1).px
    backgroundColor = Theme.Color.Background.light
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    after {
      top = (-14).px
      right = afterOffsetPx.px
      left = LinearDimension.auto
      border(7.px, BorderStyle.solid, Color.transparent)
      borderBottomColor = Theme.Color.Background.light
      position = Position.absolute
      display = Display.inlineBlock
      content = QuotedString("")
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::container }) {
    ref = props.node
    props.children()
  }
}
