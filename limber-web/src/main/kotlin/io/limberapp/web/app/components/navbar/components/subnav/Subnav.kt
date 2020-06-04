package io.limberapp.web.app.components.navbar.components.subnav

import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.component
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * Generic navigational component that drops down from a top-of-page navbar. It's generally only visible when a nav link
 * is active, but that functionality must be managed outside the scope of this component.
 *
 * By default, the subnav will be anchored on the left, with the caret on the left and extending outwards to the right.
 * If [right] is true, the subnav will be anchored on the right instead, with the caret on the right an extending
 * outwards to the left.
 *
 * [children] should be a series of [subnavGroup]s.
 */
internal fun RBuilder.subnav(right: Boolean = false, children: RHandler<RProps>) {
  child(component, Props(right), handler = children)
}

internal data class Props(val right: Boolean) : RProps

private class S : Styles("Subnav") {
  val container by css {
    height = 0.px
    width = 0.px
    alignSelf = Align.flexEnd
    position = Position.relative
  }
  private val centeringWidth = 32 // Center the caret under a component of this width.
  private val caretSize = 14 // The width of the caret. Must be even.
  private val widthPx = 224 // The width of this component.
  val subnav by css {
    position = Position.absolute
    top = 12.px
    left = 0.px
    width = widthPx.px
    backgroundColor = Theme.Color.Background.light
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    after {
      top = (-caretSize).px
      left = ((centeringWidth - caretSize) / 2).px
      right = LinearDimension.auto
      border((caretSize / 2).px, BorderStyle.solid, Color.transparent)
      borderBottomColor = Theme.Color.Background.light
      position = Position.absolute
      display = Display.inlineBlock
      content = QuotedString("")
    }
  }
  val subnavRight by css {
    left = LinearDimension.initial
    right = 0.px
    after {
      left = LinearDimension.auto
      right = ((centeringWidth - caretSize) / 2).px
    }
  }
}

private val s = S().apply { inject() }

private val component = component<Props> component@{ props ->
  div(classes = s.c { it::container }) {
    div(classes = cls(s.c { it::subnav }, s.c(props.right) { it::subnavRight })) {
      props.children()
    }
  }
}
