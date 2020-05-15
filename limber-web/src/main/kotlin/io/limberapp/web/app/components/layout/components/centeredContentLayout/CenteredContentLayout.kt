package io.limberapp.web.app.components.layout.components.centeredContentLayout

import io.limberapp.web.util.Styles
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * A layout that supports a single element or vertical group of elements, centered on the page both vertically and
 * horizontally. Content is provided by [children]. Content should be kept small; behaviour is undefined upon overflow.
 */
internal fun RBuilder.centeredContentLayout(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val styles = object : Styles("CenteredContentLayout") {
  val container by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.column
    alignItems = Align.center
    justifyContent = JustifyContent.center
    margin(16.px)
  }
}.apply { inject() }

private val component = functionalComponent<RProps> { props ->
  div(classes = styles.getClassName { it::container }) {
    props.children()
  }
}
