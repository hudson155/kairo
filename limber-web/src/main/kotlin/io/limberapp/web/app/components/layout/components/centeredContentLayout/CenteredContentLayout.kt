package io.limberapp.web.app.components.layout.components.centeredContentLayout

import io.limberapp.web.util.Styles
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.justifyContent
import kotlinx.css.margin
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
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
