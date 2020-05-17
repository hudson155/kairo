package io.limberapp.web.app.components.layout.components.standardLayout

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * The layout for most pages, supporting a vertical group of elements as the main body ([children]), and an optional
 * [leftPane] which is intended for side navigation.
 */
internal fun RBuilder.standardLayout(leftPane: ReactElement? = null, children: RHandler<Props>) {
  child(component, Props(leftPane), handler = children)
}

internal data class Props(val leftPane: ReactElement?) : RProps

private val s = object : Styles("StandardLayout") {
  val outerContainer by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.row
    justifyContent = JustifyContent.center
    margin(16.px)
    paddingTop = 32.px
  }
  val innerContainer by css {
    flexBasis = 1200.px.basis
    display = Display.flex
    flexDirection = FlexDirection.row
  }
  val leftPane by css {
    flexBasis = 256.px.basis
    display = Display.flex
    flexDirection = FlexDirection.column
    marginRight = 48.px
  }
  val mainContent by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::outerContainer }) {
    div(classes = s.c { it::innerContainer }) {
      props.leftPane?.let {
        div(classes = s.c { it::leftPane }) {
          child(it)
        }
      }
      div(classes = s.c { it::mainContent }) {
        props.children()
      }
    }
  }
}
