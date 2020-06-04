package io.limberapp.web.app.components.layout.components.centeredContentLayout

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.component
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A layout that supports a single element or vertical group of elements, centered on the page both vertically and
 * horizontally.
 *
 * Content is provided by [children]. Content should be kept small; behaviour is undefined upon overflow.
 */
internal fun RBuilder.centeredContentLayout(children: RHandler<RProps>) {
  child(component, handler = children)
}

private class S : Styles("CenteredContentLayout") {
  val container by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.column
    alignItems = Align.center
    justifyContent = JustifyContent.center
    margin(16.px)
  }
}

private val s = S().apply { inject() }

private val component = component<RProps> component@{ props ->
  div(classes = s.c { it::container }) {
    props.children()
  }
}
